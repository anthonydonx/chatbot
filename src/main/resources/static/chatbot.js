$(document).ready(function () {
   alert('hello there');
});

function getUserMessage(){
   var userMsg = $('#txtUserInput').val();
   jQuery.support.cors = true;

   var dataArray = {
      inputText: userMsg,
   }

   $.ajax({
      url: "chatbot/api/v1/response",
      type: "POST",
      contentType: "application/json; charset=utf-8",
      data: JSON.stringify(dataArray),
      success: function (response) {

      },
      error: function (xhr, status) {
         alert("Something went wrong.");
      }
   });
}