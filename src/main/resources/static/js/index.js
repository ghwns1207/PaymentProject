document.addEventListener("DOMContentLoaded", function() {
  // URL에서 쿼리 문자열을 추출합니다.
  let queryString = window.location.search;

  // URL의 쿼리 문자열을 객체로 파싱합니다.
  let params = new URLSearchParams(queryString);

  // "jwt" 파라미터의 값을 가져옵니다.
  let jwtToken = params.get('jwt');

  if(jwtToken){
    // 가져온 JWT 토큰을 확인합니다.
    console.log("JWT 토큰:", jwtToken);
    // 가져온 JWT 토큰을 세션 스토리지에 저장합니다.
    sessionStorage.setItem('jwtToken', jwtToken);
    // 페이지를 리다이렉트합니다.
    window.location.href = "/";
  }
});

function kakaoLogout() {
  // 세션 스토리지에서 JWT를 가져옵니다.
  const jwtToken = sessionStorage.getItem('jwtToken');

  // // JWT가 존재하는 경우에만 요청을 보냅니다.
  if (jwtToken) {
    fetch('http://localhost:8080/rest-api/kakao/rlogout', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${jwtToken}` // JWT를 요청 헤더에 포함합니다.
      }
    })
      .then(response => {
        sessionStorage.removeItem('jwtToken');
        // 응답을 처리합니다.
        alert("로그아웃 되었습니다.");
        window.location.href = "login";
      })
      .catch(error => {
        // 오류를 처리합니다.
        console.error(error.toString());
      });
  } else {
    alert("JWT 토큰이 세션 스토리지에 존재하지 않습니다.");
    window.location.href = "login";
  }

}


function kakaoUnlink() {

  // 세션 스토리지에서 JWT를 가져옵니다.
  const jwtToken = sessionStorage.getItem('jwtToken');

  if (jwtToken) {
    fetch("http://localhost:8080/rest-api/kakao/unlink" , {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${jwtToken}` // JWT를 요청 헤더에 포함합니다.
      }
    })
      .then(response => {
        if(response.status === 200){
          sessionStorage.removeItem('jwtToken');
          alert("회원 탈퇴 성공");
          location.href = "login";
        }
      })
      .catch(error => {
        console.error(error);
      });
  }else{
    alert("JWT 토큰이 세션 스토리지에 존재하지 않습니다.");
    window.location.href = "/login";
  }

}