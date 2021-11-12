from datetime import datetime, timedelta

from flask import Flask, render_template, jsonify, request
from pymongo import MongoClient

import hashlib
import jwt

app = Flask(__name__)

client = MongoClient("mongodb://localhost:27017/")
db = client.dbArticle

JWT_SECRET_KEY = "ARTICLE"

@app.route('/')
def index():
    return render_template('index.html')


# 회원가입
@app.route('/sign-up', methods=['POST'])
def sign_up():
    username = request.form['username']
    password = request.form['password']
    password_hash = hashlib.sha256(password.encode('utf-8')).hexdigest()

    username_exist = bool(db.users.find_one({"username": username}))
    if username_exist:
        return jsonify({'msg': '이미 있는 아이디입니다! 다른 아이디를 입력해주세요!'})

    doc = {"username": username, "password": password_hash}
    db.users.insert_one(doc)

    return {'msg': 'success'}


# 로그인
@app.route('/login', methods=['POST'])
def login():
    username = request.form['username']
    password = request.form['password']

    password_hash = hashlib.sha256(password.encode('utf-8')).hexdigest()
    result = db.users.find_one({'username': username, 'password': password_hash})

    if result is not None:
        payload = {
            'username': username,
            'exp': datetime.utcnow() + timedelta(seconds=60 * 60 * 24)  # 로그인 24시간 유지
        }
        token = jwt.encode(payload, JWT_SECRET_KEY, algorithm='HS256')

        return jsonify({'msg': 'success', 'token': token})
    else:
        return jsonify({'msg': '아이디/비밀번호가 일치하지 않습니다.'})

# 게시글 추가
@app.route('/article', methods=['POST'])
def save_post():
    token_receive = request.cookies.get('mytoken')
    username = ""
    try:
        payload = jwt.decode(token_receive, JWT_SECRET_KEY, algorithms=['HS256'])
        username = payload["username"]

    # 로그인 정보가 없는 경우
    except jwt.exceptions.DecodeError:
        username = "비회원"
    except jwt.ExpiredSignatureError:
        return jsonify({"result": "fail", "msg": "로그인 시간이 만료되었습니다!"})

    finally:
        title = request.form.get('title')
        content = request.form.get('content')
        article_count = db.article.count()
        if article_count == 0:
            max_value = 1
        else:
            max_value = db.article.find_one(sort=[("idx", -1)])['idx'] + 1

        post = {
            'idx': max_value,
            'title': title,
            'content': content,
            'read_count': 0,
            'reg_date': datetime.now(),
            'username': username
        }
        db.article.insert_one(post)
        return {"result": "success"}


# 모든 게시글 리스트
@app.route('/articles', methods=['GET'])
def get_posts():
    order = request.args.get('order')
    per_page = request.args.get('perPage')
    cur_page = request.args.get('curPage')
    search_title = request.args.get('searchTitle')
    tab = request.args.get('tab')

    # None 은 쿼리 파라미터 항목에 아예 없는 경우
    search_condition = {}
    if not search_title:
        search_condition["title"] = {"$regex": search_title}
    if not tab:
        try:
            token_receive = request.cookies.get('mytoken')
            payload = jwt.decode(token_receive, JWT_SECRET_KEY, algorithms=['HS256'])
            username = payload["username"]
            search_condition["username"] = username
        except jwt.ExpiredSignatureError:
            return jsonify({"result": "fail", "msg": "로그인 시간이 만료되었습니다!"})

    limit = int(per_page)
    skip = limit * (int(cur_page) - 1)
    total_count = db.article.find(search_condition).count()
    total_page = int(total_count / limit) + (1 if total_count % limit > 0 else 0)

    # 조회순 정렬
    if order == "desc":
        articles = list(db.article.find(search_condition, {'_id': False})
                        .sort([("read_count", -1)]).skip(skip).limit(limit))
    else:
        articles = list(db.article.find(search_condition, {'_id': False})
                        .sort([("reg_date", -1)]).skip(skip).limit(limit))

    for a in articles:
        a['reg_date'] = a['reg_date'].strftime('%Y.%m.%d %H:%M:%S')

    paging_info = {
        "totalCount": total_count,
        "totalPage": total_page,
        "perPage": per_page,
        "curPage": cur_page,
        "searchTitle": search_title
    }

    return jsonify({"result": "success", "articles": articles, "pagingInfo": paging_info})


# 게시글 삭제
@app.route('/article', methods=['DELETE'])
def delete_post():
    idx = request.args.get('idx')
    db.article.delete_one({'idx': int(idx)})
    return {"result": "success"}


# 특정 게시글 내용
@app.route('/article', methods=['GET'])
def get_post():
    idx = request.args['idx']
    article = db.article.find_one({'idx': int(idx)}, {'_id': False})
    return jsonify({"article": article})


# 게시글 업데이트
@app.route('/article', methods=['PUT'])
def update_post():
    idx = request.form.get('idx')
    title = request.form.get('title')
    content = request.form.get('content')
    db.article.update_one({'idx': int(idx)}, {'$set': {'title': title, 'content': content}})
    return {"result": "success"}


# 조회수 증가
@app.route('/article/<idx>', methods=['PUT'])
def update_read_count(idx):
    db.article.update_one({'idx': int(idx)}, {'$inc': {'read_count': 1}})
    article = db.article.find_one({'idx': int(idx)}, {'_id': False})
    return jsonify({"article": article})


# 댓글 작성
@app.route('/comment', methods=['POST'])
def save_comment():
    idx = request.form['idx']
    content = request.form['content']

    db.comment.insert_one({'content': content, 'idx': int(idx)})
    return {"result": "success"}


# 댓글 리스트
@app.route('/comment', methods=['GET'])
def get_comment():
    idx = request.args.get('idx')
    comments = list(db.comment.find({'idx': int(idx)}, {'_id': False}))

    return jsonify({"result": "success", "comments": comments})


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)