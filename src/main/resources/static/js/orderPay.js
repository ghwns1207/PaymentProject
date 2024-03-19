window.onload = function () {
  // JWT 토큰 확인
  const jwtToken = sessionStorage.getItem('jwtToken'); // 로컬 스토리지에서 JWT 토큰을 가져옵니다.
  // 요청 헤더에 JWT 토큰을 포함시킵니다.
  const goduuid = sessionStorage.getItem('goduuid');
  if (jwtToken) {
    // JWT 토큰이 있다면, 서버에 요청을 보냅니다.
    fetch(`http://localhost:8080/order/loadordersheet/${goduuid}`, {
      method: 'GET',
      headers: {
        'Authorization': 'Bearer ' + jwtToken, // JWT 토큰을 헤더에 포함시킵니다.
        'Content-Type': 'application/json',
      }
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch data');
        }
        return response.json();
      })
      .then(responseData => {
        if (responseData.resultCode === "200" || responseData.resultCode === "206") {
          // 주문 디테일 테이블에서 받아온 데이터를 처리합니다.
          console.log(responseData);
          // 여기서 데이터를 이용하여 화면에 표시하거나 다른 작업을 수행합니다.

          // // 주문자 정보 추출
          const customerInfo = responseData.data;
          console.log(customerInfo);

          // 각 입력란에 해당 정보 할당
          const nameInput = document.getElementById("nameInput");
          const phoneNumberInput = document.getElementById("phoneNumberInput");
          const emailInput = document.getElementById("emailInput");

          nameInput.value = customerInfo.purchaserName; // 예시: 주문자 이름이 name 속성에 들어있다고 가정
          phoneNumberInput.value = customerInfo.phoneNumber; // 예시: 주문자 전화번호가 phoneNumber 속성에 들어있다고 가정
          emailInput.value = customerInfo.purchaserEmail; // 예시: 주문자 이메일이 email 속성에 들어있다고 가정

          if (customerInfo.deliveryAddress !== null) {
            const deliveryAddress_recipient_name = document.getElementById("deliveryAddress_recipient_name");
            const deliveryAddress_contact_number = document.getElementById("deliveryAddress_contact_number");
            let deliveryAddress = document.getElementById("deliveryAddress");
            const deliveryAddress_id = document.getElementById("deliveryAddress_id");

            deliveryAddress_id.value = customerInfo.deliveryAddress.id;
            deliveryAddress_recipient_name.value = customerInfo.deliveryAddress.recipientName;
            deliveryAddress_contact_number.value = customerInfo.deliveryAddress.contactNumber;
            deliveryAddress.value = customerInfo.deliveryAddress.roadAddress + customerInfo.deliveryAddress.detailAddress
              + " " + customerInfo.deliveryAddress.extraAddress + ` (${customerInfo.deliveryAddress.postalCode})`;

          }

          displayCartItems(responseData.data.orderDetailsList);
        } else {
          alert(responseData.errorMessage);
        }

      })
      .catch(error => {
        console.error('Error:', error);
      });

  } else {
    if (confirm('로그인이 필요한 서비스입니다. 로그인 하시겠습니까?.')) {
      window.location = "http://localhost:8080/login"
      return;
    }
  }
};

// 배송지 업데이트
document.getElementById("updateDeliveryAddressBtn").addEventListener('click', () => {

  // JSON 객체 출력 (예시)
  console.log(deliveryAddress);

  // 세션 스토리지에서 JWT 토큰을 가져옵니다.
  let jwtToken = sessionStorage.getItem('jwtToken');

  if (!jwtToken) {
    if (confirm('로그인이 필요한 서비스입니다. 로그인 하시겠습니까?.')) {
      window.location = "http://localhost:8080/login";
    } else {
      return; // 함수를 종료합니다.
    }
  } else {
    // 요소들을 JSON 객체로 합치기
    let deliveryAddress = {
      postcode: document.getElementById("postcode").value, // 우편번호
      roadAddress: document.getElementById("roadAddress").value, // 도로명주소
      detailAddress: document.getElementById("detailAddress").value, // 상세주소
      extraAddress: document.getElementById("extraAddress").value, // 참고항목
      recipient_name: document.getElementById("recipient_name").value, // 받는 이
      contact_number: document.getElementById("contact_number").value, // 연락처
      default_address: document.getElementById("default_address").checked // 연락처
    };

    console.log(deliveryAddress);
    // 요청 헤더에 JWT 토큰을 포함시킵니다.
    let headers = {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + jwtToken
    };

    // 배송지 추가
    fetch(`http://localhost:8080/delivery_address/ordersheet/adddelivery`, {
      method: "PUT",
      headers: headers, // 헤더에 JWT 토큰을 포함시킵니다.
      body: JSON.stringify(deliveryAddress),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch data');
        }
        return response.json();
      })
      .then(responseDate => {
        console.log(responseDate.data);
        if (responseDate.resultCode === "200") {
          alert("배송지가 추가 되었습니다.");
          retrieveDeliveryAddress(headers);
        } else if (responseDate.resultCode === "401") {
          alert(responseDate.resultMessage);
        } else {
          alert(responseDate.errorMessage);
        }
      })
      .catch(error => {
        console.error(error);
      });
  }
});


// 모달 열기 버튼 클릭 이벤트 핸들러
document.getElementById('openModalBtn').addEventListener('click', function () {
  document.getElementById('myModal').style.display = 'block';

  // 세션 스토리지에서 JWT 토큰을 가져옵니다.
  let jwtToken = sessionStorage.getItem('jwtToken');

  if (!jwtToken) {
    if (confirm('로그인이 필요한 서비스입니다. 로그인 하시겠습니까?.')) {
      window.location = "http://localhost:8080/login";
      return;
    } else {
      return; // 함수를 종료합니다.
    }
  } else {

    let headers = {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + jwtToken
    };
    retrieveDeliveryAddress(headers);
  }
});

// 배송지 주소 불러오기
function retrieveDeliveryAddress(headers) {
  // 배송지 주소 리스트
  fetch("/delivery_address/ordersheet/retrieveDeliveryAddress", {
    method: "GET",
    headers: headers,
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Failed to fetch data');
      }
      return response.json();
    })
    .then(responseDate => {
      if (responseDate.resultCode !== "200") {
        alert(responseDate.errorMessage);
      }
      if(responseDate.data !== null){
        createAddressListItem(responseDate.data);
      }
    })
    .catch(error => {
      console.error(error);
    });
}

// 배송지 리스트 li 추가
function createAddressListItem(addressList) {
  let deliveryAddressListUl = document.getElementById("deliveryAddressListUl");
  deliveryAddressListUl.innerHTML = "";
  addressList.forEach((address) => {
    let addressHTML = `
      <div>
      <span class="checkedAddressData" style="display: none">${address.id}</span>
        <span class="checkedAddressData" >${address.recipientName}${address.shippingName != null ? address.shippingName : ""}
        ${address.defaultAddress == true ? "(기본배송지)" : ""}
        </span><button type="button" onclick="selectedAddress(${address.id}, this)">선택</button>
        <p class="checkedAddressData">${address.contactNumber}</p>
        <span class="checkedAddressData" >${address.roadAddress} ${address.detailAddress} ${address.extraAddress} (${address.postalCode})</span>
        <div>
          <button type="button" onclick="updateDeliveryAddress(${address.id})">수정</button>
          <button type="button" onclick="deleteDeliveryAddress(${address.id}, ${address.defaultAddress})">삭제</button>
        </div>
      </div>
    `;
    let listItem = document.createElement("li");

    listItem.innerHTML = addressHTML;
    deliveryAddressListUl.appendChild(listItem);

  });
}

// 배송지 선택 이벤트
function selectedAddress(addressId, target) {
  let parentNode = target.parentNode; // 부모 노드 찾기
  let childrens = parentNode.getElementsByClassName("checkedAddressData"); // 자식 요소들 가져오기
  let childrenData = [];

  // HTMLCollection을 배열로 변환하여 forEach 메서드 사용
  Array.from(childrens).forEach(child => {
    childrenData.push(child.innerText);
  });

  // 각 입력란에 해당 정보 할당
  const recipient_name = document.getElementById("deliveryAddress_recipient_name");
  const contact_number = document.getElementById("deliveryAddress_contact_number");
  const deliveryAddress = document.getElementById("deliveryAddress");
  const deliveryAddress_id = document.getElementById("deliveryAddress_id");

  deliveryAddress_id.value = childrenData[0];
  recipient_name.value = childrenData[1];
  contact_number.value = childrenData[2];
  deliveryAddress.value = childrenData[3];
  document.getElementById('myModal').style.display = 'none';

  console.log(deliveryAddress_id.value);
}


// 배송지 주소 업데이트 by id and user_id
function updateDeliveryAddress(addressId) {

  // 세션 스토리지에서 JWT 토큰을 가져옵니다.
  let jwtToken = sessionStorage.getItem('jwtToken');
  let headers = {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + jwtToken
  };
  // 배송지 주소 업데이트
  fetch(`/delivery_address/ordersheet/updateDeliveryAddress/${addressId}`, {
    method: "PUT",
    headers: headers,
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Failed to fetch data');
      }
      return response.json();
    })
    .then(responseDate => {
      console.log(responseDate.data);
      if (responseDate.resultCode !== "200") {
        alert(responseDate.errorMessage);
      }
      retrieveDeliveryAddress(headers);
    })
    .catch(error => {
      console.error(error);
    });


}


// 배송지 주소 삭제 by id and user_id
function deleteDeliveryAddress(addressId, checkDefault) {

  if (checkDefault) {
    alert("다른 배송지를 기본배송지로 설정 후 삭제해주세요.");
    return;
  } else {
    // 세션 스토리지에서 JWT 토큰을 가져옵니다.
    let jwtToken = sessionStorage.getItem('jwtToken');
    let headers = {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + jwtToken
    };
    // 배송지 주소 삭제
    fetch(`/delivery_address/ordersheet/deleteDeliveryAddress/${addressId}`, {
      method: "DELETE",
      headers: headers,
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch data');
        }
        return response.json();
      })
      .then(responseDate => {
        console.log(responseDate.data);
        if (responseDate.resultCode !== "200") {
          alert(responseDate.errorMessage);
        }
        retrieveDeliveryAddress(headers);
      })
      .catch(error => {
        console.error(error);
      });
  }
}

// 모달 닫기 버튼 및 모달 외부 클릭 이벤트 핸들러
let modal = document.getElementById('myModal');
let closeBtn = document.getElementsByClassName('close')[0];
let searchDeliveryAddressDiv = document.getElementById("searchDeliveryAddressDiv");
closeBtn.addEventListener('click', function () {
  modal.style.display = 'none';
  searchDeliveryAddressDiv.style.display = 'none';
});

window.addEventListener('click', function (event) {
  if (event.target == modal) {
    modal.style.display = 'none';
  }
});

document.getElementById("addressCheckBTN").addEventListener('click', () => {
  document.getElementById('recipient_nameDiv').style.display = 'block';
  document.getElementById('contact_numberDiv').style.display = 'block';
})

// 주소 스크립트
function execDaumPostcode() {

  document.getElementById("searchDeliveryAddressDiv").style.display = 'block';

  new daum.Postcode({
    oncomplete: function (data) {
      // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

      // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
      // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
      let roadAddr = data.roadAddress; // 도로명 주소 변수
      let extraRoadAddr = ''; // 참고 항목 변수

      // 법정동명이 있을 경우 추가한다. (법정리는 제외)
      // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
      if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
        extraRoadAddr += data.bname;
      }
      // 건물명이 있고, 공동주택일 경우 추가한다.
      if (data.buildingName !== '' && data.apartment === 'Y') {
        extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
      }
      // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
      if (extraRoadAddr !== '') {
        extraRoadAddr = ' (' + extraRoadAddr + ')';
      }

      // // 우편번호와 주소 정보를 해당 필드에 넣는다.
      document.getElementById('postcode').value = data.zonecode;
      document.getElementById("roadAddress").value = roadAddr;

      // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
      if (roadAddr !== '') {
        document.getElementById("extraAddress").value = extraRoadAddr;
        document.getElementById("roadFullAddress").value = roadAddr + extraRoadAddr + `(${data.zonecode})`;
        document.getElementById("detailAddress").value = "";

      } else {
        document.getElementById("extraAddress").value = '';
        document.getElementById("roadFullAddress").value = roadAddr + extraRoadAddr + `(${data.zonecode})`;
        document.getElementById("detailAddress").value = "";

      }

      let guideTextBox = document.getElementById("guide");
      // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
      if (data.autoRoadAddress) {
        let expRoadAddr = data.autoRoadAddress + extraRoadAddr;
        guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
        guideTextBox.style.display = 'block';

      } else if (data.autoJibunAddress) {
        let expJibunAddr = data.autoJibunAddress;
        guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
        guideTextBox.style.display = 'block';
      } else {
        guideTextBox.innerHTML = '';
        guideTextBox.style.display = 'none';
      }
    }
  }).open();
}

// 장바구니 목록 테이블 추가 함수
function displayCartItems(orderItems) {
  const orderItemsBody = document.getElementById("orderItemsBody");
  orderItemsBody.innerHTML = ""; // 기존 테이블 내용 초기화

  orderItems.forEach((orderItems, index) => {
    const row = document.createElement("tr");
    row.innerHTML = `
                <td><input class="ordertId" readonly value="${orderItems.id}"></td>
                <td><input class="orderItemId" type="text" readonly value="${orderItems.productId}"></td>
                <td><input class="orderItemCount"  type="number" readonly value="${orderItems.productQuantity}"></td>
                <td><input class="orderItemPrice"  type="text" readonly value="${orderItems.totalPrice}"></td>
      `;
    orderItemsBody.appendChild(row);
  });
}

function kakaoPayReady() {

  let jwtToken = sessionStorage.getItem('jwtToken');
  let goduuid = sessionStorage.getItem('goduuid');
  let headers = {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + jwtToken
  };

  const delivery_address_id = document.getElementById("deliveryAddress_id").value;
  console.log(delivery_address_id);
  if (!delivery_address_id){
    alert("배송지 정보가 없습니다");
    return;
  }

  if (jwtToken) {
    // 비동기 통신
    fetch(`/kakao-pay/ready/${goduuid}/${delivery_address_id}`, {
      method: 'POST',
      headers: headers,
    })
      .then(response => response.json())
      .then(data => {
        location.href = data.redirectUrl;
      })
      .catch(error => {
        console.error('Error:', error);
      });
  }

}
