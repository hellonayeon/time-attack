from flask import Flask, render_template, jsonify, request
app = Flask(__name__)

from pymongo import MongoClient
client = MongoClient('localhost', 27017)
db = client.dbStock


@app.route('/')
def home():
    return render_template('index.html')

@app.route('/base/code')
def search_groups():
    group_list = list(db.codes.distinct("group"))

    return jsonify({'groups': group_list}, {'_id': False})

@app.route('/code')
def search():
    group_receive = request.args.get('group_give')
    contents = list(db.codes.find({'group': group_receive}, {'_id': False}))

    return jsonify({'contents': contents}, {'_id': False})

if __name__ == '__main__':
    app.run('0.0.0.0', port=5000, debug=True)
