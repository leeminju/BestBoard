const host = 'http://' + window.location.host;

var imageFile;
var updateFile;


// 화면 시작하자마자
$(document).ready(function () {
    // 첨부파일을 변경할때 마다 실행되는 이벤트
    $("#create_image").on("change", function (event) {
        imageFile = event.target.files[0];
    });

    $("#edit_image").on("change", function (event) {
        updateFile = event.target.files[0];
    });

    authorizationCheck();//인가
    addPostCard();//게시글 카드 추가
})

// 인가 : 토큰 유효성 판단
function authorizationCheck() {
    const auth = getToken();

    if (auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', auth);
        });
    } else {
        window.location.href = host + '/api/users/login-page';
        return;
    }

    //로그인한 회원 정보
    $.ajax({
        type: 'GET',
        url: `/api/user-info`,
        contentType: 'application/json',
    })
        .done(function (res, status, xhr) {
            const nickname = res;

            if (!nickname) {
                window.location.href = '/api/users/login-page';
                return;
            }

            $('#nickname').text(nickname);
        })
        .fail(function (jqXHR, textStatus) {
            logout();
        });
}

function logout() {
    // 토큰 삭제
    Cookies.remove('Authorization', {path: '/'});
    window.location.href = host + '/api/user/login-page';
}

function getToken() {
    let auth = Cookies.get('Authorization');

    if (auth === undefined) {
        return '';
    }

    return auth;
}

//게시글 카드 추가
function addPostCard() {
    $.ajax({
        type: 'GET',
        url: '/api/posts',
        contentType: 'application/json',
        success: function (response) {
            for (var i = 0; i < response.length; i++) {
                let post = response[i];
                let id = post['id'];
                let title = post['title'];
                let contents = post['contents'];
                let createdAt = post['createdAt'];
                let nickname = post['nickname'];
                let image = post['image'];
                let commentCount = post['commentCount'];
                let likeCount = post['likeCount'];

                if (image === null) {
                    image = "images/default.jpg";
                }

                let html =
                    `<div onclick="showDetails('${id}')" style="transition:transform 0.3s ease-in-out" class="col" data-bs-toggle="modal" data-bs-target="#PostDetailsModal">
                     <img src="${image}" class="card-img-top" alt="..." style="object-fit:contain;height:400px;width: 100%">

                    <div class="post_content" style="margin: 10px 10px 0px 10px">
                    <div>
                    <button class="like_btn" id="like-btn-${id}" type="button"></button><span style="font-size: 25px;margin-left: 10px">${likeCount}</span>
                    </div>
                    <h5>${title}</h5>
                    <h5>댓글 ${commentCount}</h5>
                    <h5>${nickname}</h5>
                    <h5>${createdAt}</h5>
                    </div> 
                   </div>`

                checkLike2(id);
                $('#card').append(html);

            }
        }
        ,
        error(error, status, request) {
            console.log(error);
        }
    });
}


//게시글 작성
function createPost(username) {
    let formData = new FormData();

    if (!imageFile) {
        formData.append('image', null);
    } else {
        formData.append('image', imageFile);
    }

    let title = $('#create_title').val();
    let contents = $('#create_contents').val().replace("\r\b", "<br>");//textarea에서 엔터-> <br>로 변환

    let data = {
        'title': title,
        'contents': contents
    };
    formData.append("data", new Blob([JSON.stringify(data)], {type: "application/json"}));


    $.ajax({
            type: 'POST',
            url: '/api/posts',
            data: formData,
            contentType: false,
            processData: false,
            success: function (response) {
                alert(response['responseMessage']);
                window.location.reload();
            },
            error(error, status, request) {
                alert(error['responseJSON']['responseMessage']);
            }
        }
    );
}

//게시물 세부 내용 출력
function showDetails(id) {
    //게시글 내용 출력
    $.ajax({
        type: 'GET',
        url: `/api/posts/${id}`,
        success: function (response) {
            let id = response['id'];
            let title = response['title'];
            let nickname = response['nickname'];
            let contents = response['contents'];
            let createdAt = response['createdAt'];
            let image = response['image'];
            let likeCount = response['likeCount'];

            contents = contents.replaceAll("<br>", "\r\n");
            $('#like_count').text(likeCount);
            $('#response_title').text(title);
            $('#response_contents').text(contents);
            $('#response_nickname').text(nickname);
            $('#response_createdAt').text(createdAt);
            if (image === null) {
                $('#response_image').hide();
                $('#image_remove_btn').hide();
            } else {
                $('#response_image').attr("src", image);
                $('#image_remove_btn').show().attr("disabled", false);
            }

            $('#delete_btn').val(id);
            $('#update_btn').val(id);
            $('#image_remove_btn').val(image);//기존의 이미지 정보

            $('#edit_title').val(title);
            $('#edit_contents').val(contents);
        }
    })
    checkLike(id);
    showComment(id);
}

function checkLike(id) {
    //로그인한 유저가 좋아요 했는지 확인
    $.ajax({
            type: 'GET',
            url: `/api/likes/${id}`,
            success: function (response) {
                if (response) {
                    $('#like_btn').css({"background": "url(/images/full.png)"});
                } else {
                    $('#like_btn').css({"background": "url(/images/empty.png)"});
                }

            },
            error(error, status, request) {
                alert(error['responseJSON']['responseMessage']);
            }

        }
    )
}

function checkLike2(id) {
    //로그인한 유저가 좋아요 했는지 확인
    $.ajax({
            type: 'GET',
            url: `/api/likes/${id}`,
            success: function (response) {
                if (response) {
                    $(`#like-btn-${id}`).css({"background": "url(/images/full.png)"}).val(id);
                } else {
                    $(`#like-btn-${id}`).css({"background": "url(/images/empty.png)"}).val(id);
                }

            },
            error(error, status, request) {
                alert(error['responseJSON']['responseMessage']);
            }

        }
    )
}

function likePost() {
    let id = $('#update_btn').val();
    $.ajax({
            type: 'POST',
            url: `/api/likes/${id}`,
            success: function (response) {
                if (response) {
                    $('#like_btn').css({"background": "url(/images/full.png)"});
                } else {
                    $('#like_btn').css({"background": "url(/images/empty.png)"});
                }

            },
            error(error, status, request) {
                alert(error['responseJSON']['responseMessage']);
            }

        }
    )
}



//기존의 이미지 삭제
function removeOriginal() {
    $('#image_remove_btn').val(null).attr("disabled", true);
}


//게시물 업데이트(Save 버튼 클릭 시)
function updatePost() {
    let original = false;
    let formData = new FormData();
    var original_image = $('#image_remove_btn').val();

    if (!original_image) {
        original = false;//기존 이미지 삭제됨
    } else {
        original = true;//기존 이미지 유지
    }


    if (!updateFile) {
        formData.append('image', null);
    } else {
        formData.append('image', updateFile);
    }

    let id = $('#update_btn').val();
    let title = $('#edit_title').val();
    let contents = $('#edit_contents').val().replace("\r\b", "<br>");

    let data = {
        'title': title,
        'contents': contents
    };
    console.log(original);
    formData.append("data", new Blob([JSON.stringify(data)], {type: "application/json"}));
    $.ajax({
            type: 'PUT',
            url: `/api/posts/${id}/${original}`,
            data: formData,
            contentType: false,
            processData: false,
            success: function (response) {
                alert(response['responseMessage']);
                window.location.reload();
            },
            error(error, status, request) {
                alert(error['responseJSON']['responseMessage']);
                showDetails(id);
            }
        }
    );
}

//할일 삭제(삭제 버튼 클릭)
function deletePost() {
    let id = $('#delete_btn').val();

    $.ajax({
            type: 'DELETE',
            url: `/api/posts/${id}`,
            contentType: 'application/json',
            success: function (response) {
                alert(response['responseMessage']);
                window.location.reload();
            },
            error(error, status, request) {
                alert(error['responseJSON']['responseMessage']);
            }
        }
    );
}

//modal 내 close 버튼 클릭 시 새로고침
function win_reload() {
    window.location.reload();
}

//댓글 생성
function create_Comment() {
    let post_id = $('#delete_btn').val();
    let contents = $('#comment_text').val().replace("\r\b", "<br>");

    let data = {"contents": contents};

    $.ajax({
            type: 'POST',
            url: `/api/posts/${post_id}/comments`,
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                alert(response['responseMessage']);
                showComment(post_id);
            },
            error(error, status, request) {
                alert(error['responseJSON']['responseMessage']);
            }
        }
    );
}

//할 일내 댓글 조회
function showComment(post_id) {
    $.ajax({
        type: 'GET',
        url: `/api/posts/${post_id}/comments`,
        success: function (response) {
            $('#comment-card').empty();

            for (var i = 0; i < response.length; i++) {
                let comment = response[i];

                let comment_id = comment['id'];
                let createdAt = comment['createdAt'];
                let nickname = comment['nickname'];
                let contents = comment['contents'].replaceAll("<br>", "\r\n");

                let tempHTML = `<div class="card mb-4">
                                <div class="card-body">
                                    <div id="${comment_id}-text" class="comment_text">
                                        ${contents}
                                    </div>
                                    <div id="${comment_id}-editarea" class="edit_area">
                                        <textarea id="${comment_id}-textarea" placeholder="댓글 내용" class="edit_textarea" name="" id="" rows="2">${contents}</textarea>
                                    </div>
                                    <div class="d-flex justify-content-between">
                                        <div class="d-flex flex-row align-items-center">                                        
                                            <p class="small mb-0 ms-2">${nickname}</p>
                                        </div>
                                        <div class="d-flex flex-row align-items-center">
                                            <p class="small text-muted mb-0">${createdAt}</p>
                                        </div>    
                                    </div>
                                    <div class="footer">
                                            <img id="${comment_id}-edit" class="icon-start-edit" src="images/edit.png" alt="" onclick="editComment('${comment_id}')">
                                            <img id="${comment_id}-delete" class="icon-delete" src="images/delete.png" alt="" onclick="delete_Comment('${comment_id}','${post_id}')">
                                            <img id="${comment_id}-submit" class="icon-end-edit" src="images/done.png" alt="" onclick="submitEdit('${comment_id}','${post_id}')">
                                    </div>
                                </div>
                            </div>`;

                $('#comment-card').append(tempHTML);
            }
        }
    })
}

//편집 버튼 누르면 -> 편집 공간 display
function editComment(id) {
    showEdits(id);
}

function showEdits(id) {
    $(`#${id}-editarea`).show();
    $(`#${id}-submit`).show();
    $(`#${id}-delete`).show();

    $(`#${id}-text`).hide();
    $(`#${id}-edit`).hide();
}

//댓글 삭제
function delete_Comment(id, post_id) {
    //삭제 API 호출
    $.ajax({
        type: "DELETE",
        url: `/api/comments/${id}`,
        contentType: "application/json",
        success: function (response) {
            alert(response['responseMessage']);
            showComment(post_id);
        }, error(error, status, request) {
            alert(error['responseJSON']['responseMessage']);
            showComment(post_id);
        }

    });

}

//댓글 수정 제출
function submitEdit(id, post_id) {

    let contents = $(`#${id}-textarea`).val().replaceAll("<br>", "\r\n");

    let data = {'contents': contents};

    $.ajax({
        type: "PUT",
        url: `/api/comments/${id}`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (response) {
            alert(response['responseMessage']);
            showComment(post_id);
        }, error(error, status, request) {
            alert(error['responseJSON']['responseMessage']);
            showComment(post_id);
        }

    });


}