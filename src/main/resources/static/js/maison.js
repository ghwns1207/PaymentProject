"use strict";

document.addEventListener("DOMContentLoaded", function () {
  const indicators = document.querySelectorAll("indicator");
  const sections = document.querySelectorAll("section");
  const body = document.querySelector("body");
  const pc_header = body.querySelector(".pc-header-nav");

  function toggleHeaderClass() {
    const scrollY = window.scrollY || window.pageYOffset;

    if (scrollY !== 0) {
      pc_header.classList.add("active");
    } else {
      pc_header.classList.remove("active");
    }
  }

  // 스크롤 이벤트에 대한 처리
  window.addEventListener("scroll", toggleHeaderClass);

  // 마우스 오버 이벤트에 대한 처리
  pc_header.addEventListener("mouseover", function () {
    pc_header.classList.add("active");
  });

  // 마우스 아웃 이벤트에 대한 처리
  pc_header.addEventListener("mouseout", function () {
    const scrollY = window.scrollY || window.pageYOffset;

    if (scrollY === 0) {
      pc_header.classList.remove("active");
    }
  });

  // ======================================================

});


