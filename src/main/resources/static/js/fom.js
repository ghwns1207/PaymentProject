const form = document.querySelector('form'),
  inputVail = form.querySelector(".inputvail"),
  valip = form.querySelector('.valip'),
  agreement = form.querySelector('#agreement'),
  ptag = valip.querySelector("P");

const phoneNumberRegex = /^\d{2,3}-\d{4}-\d{4}$/;

let trigger = false;
let checkTrigger = false;

agreement.addEventListener("change", function () {
  if (agreement.checked) {
    checkTrigger = true;
  } else {
    checkTrigger = false;
  }
});

inputVail.addEventListener('input', (e) => {
  const eventObj = e.target;

  eventObj.value = eventObj.value
    .replace(/[^0-9]/g, '')
    .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(\-{1,2})$/g, "");

  // eventObj.value = eventObj.value
  //   .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/, function(match, p1, p2, p3) {
  //     var parts = [p1, p2, p3];
  //     parts = parts.map(function(part) {
  //       return part.replace(/\D/g, '');  // 숫자 이외의 문자 제거
  //     });
  //     return parts.filter(function(part) {
  //       return part !== '';  // 빈 문자열 제거
  //     }).join('-');
  //   });

  if (phoneNumberRegex.test(eventObj.value)) {
    trigger = true;
    ptag.textContent = "정확한 입력 감사합니다. 다음 단계로 진행하세요."
    ptag.style.color = "green";
  } else {
    trigger = false;
    ptag.textContent = "연락처는 010-xxxx-xxxx와 같은 올바른 형식으로 입력해 주세요."
    ptag.style.color = "red";
  }
});

document.addEventListener('DOMContentLoaded', function() {
  grecaptcha.ready(function() {
    grecaptcha.execute('6LcfhlQpAAAAAI-TP8BLxJEAHD7Q1Mu9L6O6i_ni', { action: 'submit' }).then(function(token) {

      // Set a listener for the form submit event
      document.getElementById('myForm').addEventListener('submit', function(event) {
        event.preventDefault(); // Prevent the default form submission
        if(trigger && checkTrigger){
          // Use the token and submit the form
          fetch('/VerifyRecaptcha', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({ token: token }),
          })
            .then(response => response.json())
            .then(result => {
              console.log(result);

              // If reCAPTCHA verification is successful, submit the form
              if (result.success) {
                document.getElementById('myForm').submit();
              } else {
                // Handle the case where reCAPTCHA verification fails
                console.error('reCAPTCHA verification failed');
              }
            })
            .catch(error => {
              console.error('Error:', error);
            });
        }else {
          if(!trigger) alert("연락처를 확인해주세요.")
          if(!checkTrigger) alert("개인정보 제공 및 활용에 대한 동의해주세요.");

        }

      });
    });
  });
});

