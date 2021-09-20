from flask import Flask, render_template, jsonify, request
app = Flask(__name__)

from pymongo import MongoClient
client = MongoClient('localhost', 27017)
db = client.dbStock

import requests
from bs4 import BeautifulSoup

# 문자열 전처리
import re

# 'codes' 그룹 리스트
# 이 정보를 클라이언트쪽에서 갖고 있어야 하는가? 서버 쪽에서 갖고 있어야 하는가?
# 그룹들에 대한 정보는 모든 사용자에게 동일하게 적용되므로, 서버에서 그룹 리스트를 관리하는게 맞다고 판단
# 클라이언트에서 이 정보에 대해 요청해서 이 정보를 갖고 있는 다는 것은 비효율적이라 생각 (왜? 다 똑같이 가지고 있어야 하는 값이며, 사용자에따라 변하지 않는 값이니까)
groups = list(db.codes.distinct("group"))

@app.route("/", methods=["GET"])
def home():
    return render_template('index.html')

@app.route("/code", methods=["POST"])
def get_group_info():
    receive = request.json
    group_idx_receive = int(receive["group_idx_give"])
    input_value_receive = receive["input_value_give"]

    print(f"receive = {group_idx_receive, input_value_receive}")

    # group_info = [] # 딕셔너리에 대한 리스트
    # if input_value_receive == '':
    #     group_info = list(db.codes.find({"group": groups[group_idx_receive]}, {"_id": False}))
    # else :
    #     # 사용자가 선택한 이전의 필드 값을 바탕으로 다음 그룹에 해당되는 리스트 추출
    #     # ex) market-2의 경우 sector-1, sector-2 가 있음 (sector-3는 없음)
    #     src = groups[group_idx_receive-1]
    #     target = groups[group_idx_receive]
    #     print(f"db search condition = {src, target}")
    #     search_key = list(db.stocks.distinct(target, {src: input_value_receive}))
    #     print(f"search key = {search_key}")
    #     doc = {}
    #
    #     group_info = list(db.codes.find({"code": {"$in": search_key}}, {"_id": False}))
    #     print(type(group_info))
    #
    # print(group_info)

    group_info = list(db.codes.find({"group": groups[group_idx_receive]}, {"_id": False}))
    return jsonify(group_info)


# 1. 사용자가 입력한 code('codes' collection)들을 바탕으로
# stocks 컬렉션에서 code('stocks' collection)을 찾는다.
# 2. 찾은 code('stocks' collection)을 바탕으로 웹 스크래핑 수행
@app.route('/stock', methods=["POST"])
def get_stock_info():
    codes_receive = request.json
    print(codes_receive)
    stocks = list(db.stocks.find({"market": codes_receive[0], "sector": codes_receive[1], "tag": codes_receive[2]}, {"_id": False}))
    print(stocks)

    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36'}

    stock_info = []
    for stock in stocks:
        data = requests.get(f"https://finance.naver.com/item/main.nhn?code={stock['code']}", headers=headers)  # headers=headers 브라우저에서 검색한 것 마냥 만들어주는 속성

        soup = BeautifulSoup(data.text, 'html.parser')

        # 종목명
        name = stock["name"]
        # 주가 총액
        stock_price = soup.select("#chart_area > div.rate_info > div > p.no_today > em > span.blind")[0].text
        # 시가 총액
        market_price = re.sub('[^A-Za-z0-9가-힣]', '', soup.select("#_market_sum")[0].text)
        # PER
        PER = soup.select("#_per")[0].text

        doc = {
            "name": name,
            "stock_price": stock_price,
            "market_price": market_price,
            "PER": PER
        }
        stock_info.append(doc)

    print(stock_info)

    return jsonify(stock_info)

@app.route("/bookmark", methods=["POST"])
def add_bookmark():
    info_receive = request.json
    print(info_receive)
    db.bookmark.insert_one(info_receive)

    return jsonify({"msg": "즐겨찾기 추가 완료 !"})


@app.route("/bookmark", methods=["GET"])
def bookmark():
    return render_template("bookmark.html")

@app.route("/bookmark/list", methods=["GET"])
def get_bookmark():
    bookmarks = list(db.bookmark.find({}, {"_id": False}))
    print(bookmarks)

    return jsonify(bookmarks)

@app.route("/bookmark/delete", methods=["POST"])
def delete_bookmark():
    name_receive = request.form["name_give"]
    db.bookmark.delete_many({"name": name_receive})

    return jsonify({"msg": "즐겨찾기 취소 완료 !"})

if __name__ == '__main__':
    app.run('0.0.0.0', port=5000, debug=True)
