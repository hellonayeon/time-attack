from flask import Flask, render_template, jsonify, request
app = Flask(__name__)

from pymongo import MongoClient
client = MongoClient('localhost', 27017)
db = client.dbStock

import requests
from bs4 import BeautifulSoup

# 문자열 전처리
import re

@app.route("/", methods=["GET"])
def home():
    return render_template('index.html')

@app.route("/code/base", methods=["GET"])
def set_base_code():
    groups = list(db.codes.distinct("group"))
    return jsonify({"all_group": groups})

@app.route("/code", methods=["GET"])
def get_group_info():
    group = request.args.get("group")
    print(group)

    group_info = list(db.codes.find({"group": group}, {"_id": False}))
    return jsonify(group_info)

@app.route('/stocks', methods=["POST"])
def get_stocks_info():
    codes = request.json
    print(codes)
    stocks = list(db.stocks.find(codes, {"_id": False}))
    return jsonify(stocks)

@app.route('/stock', methods=["GET"])
def get_stocks_detail():
    code = request.args.get("code")

    headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36'}
    data = requests.get(f"https://finance.naver.com/item/main.nhn?code={code}", headers=headers)  # headers=headers 브라우저에서 검색한 것 처럼 만들어주는 속성
    soup = BeautifulSoup(data.text, 'html.parser')

    stock_price = soup.select("#chart_area > div.rate_info > div > p.no_today > em > span.blind")[0].text
    market_price = re.sub('[^A-Za-z0-9가-힣]', '', soup.select("#_market_sum")[0].text)
    PER = soup.select("#_per")[0].text

    return jsonify({"stock_price": stock_price, "market_price": market_price, "PER": PER})

@app.route("/bookmark", methods=["POST"])
def add_bookmark():
    code = request.form["code"]
    print(code)
    stock = db.stocks.find_one({"code": code}, {"_id": False})
    print(stock)
    db.bookmark.insert_one(stock)

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
    code = request.form["code"]
    db.bookmark.delete_many({"code": code})

    return jsonify({"msg": "즐겨찾기 취소 완료 !"})

if __name__ == '__main__':
    app.run('0.0.0.0', port=5000, debug=True)
