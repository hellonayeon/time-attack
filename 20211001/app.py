from flask import Flask, render_template, jsonify, request
app = Flask(__name__)

from pymongo import MongoClient
client = MongoClient('localhost', 27017)
db = client.dbmemo

import datetime

global memo_idx
memo_idx = 0

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

    global memo_idx
    memo_info['index'] = memo_idx
    memo_idx += 1

    # 메모 작성 날짜 시간
    date = datetime.datetime.now().strftime('%Y.%m.%d %H:%M:%S')
    memo_info['date'] = date

    db.memo.insert_one(memo_info)

    return jsonify({'msg': 'success'})

# index = memo id
@app.route('/memo/delete', methods=['POST'])
def delete_memo():
    index = int(request.form['index'])
    db.memo.delete_one({"index": index})

    return jsonify({'msg': 'success'})

if __name__ == '__main__':
    app.run('0.0.0.0', port=5000, debug=True)