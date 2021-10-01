from flask import Flask, render_template, jsonify, request
app = Flask(__name__)

from pymongo import MongoClient
client = MongoClient('localhost', 27017)
db = client.dbmemo

import datetime

@app.route('/')
def home():
    return render_template('index.html')

@app.route('/memo', methods=['GET'])
def get_memo():
    memos = list(db.memo.find({}, {"_id": False}))
    return jsonify(memos)

@app.route('/memo', methods=['POST'])
def add_memo():
    memo_info = request.json

    # 메모 작성 날짜 시간
    date = datetime.datetime.now().strftime('%Y.%m.%d %H:%M:%S')
    memo_info['date'] = date

    db.memo.insert_one(memo_info)

    return jsonify({'msg': 'success'})

@app.route('/memo/delete', methods=['POST'])
def delete_memo():
    date = request.form['date']
    db.memo.delete_one({"date": date})

    return jsonify({'msg': 'success'})

if __name__ == '__main__':
    app.run('0.0.0.0', port=5000, debug=True)