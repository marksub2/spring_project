document.getElementById("buttonSubmit").addEventListener("click",function(){
	//객체
	const formData = {
	    memID: document.getElementById("memID").value,
	    title: document.getElementById("title").value,
	    content: document.getElementById("content").value,
	    writer: document.getElementById("writer").value,
	
	};

	console.log(formData);


//index.jsp파일에서 만든 메타CSRF태그두개를 js파일로 가져온다.
const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

   fetch('/menu/add', {
        method: 'POST',
        headers: {
			'Content-Type': 'application/json',
            [csrfHeader]: csrfToken // CSRF 헤더와 토큰을 동적으로 추가
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('공지사항 작성실패');
        }
        return response.text(); // 게시글 잘 작성됨.
    })
    .then(data => {
        console.log('Success:', data);
        // 공지사항 조회 페이지로 리다이렉트
        window.location.href = '/';
    })
    .catch(error => {
        console.error('Error가 발생:', error);
    });
});