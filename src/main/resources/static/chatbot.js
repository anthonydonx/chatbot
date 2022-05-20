var selCourseId = "";
var selFacultyId = "";
$(document).ready(function () {
    $("#chatButton").click(function () {
        $("#popupDiv").toggleClass('display-none', 1000, 'easeOutSine');
    });
});
$('#txtUserInput').keydown(function (e) {
    if (e.keyCode == 13) {
        handleInput();
    }

});

function handleInput() {
    var userMsg = $('#txtUserInput').val();
    if (!userMsg || userMsg.length === 0) {
        userMsg = " ";
        addMyChatToContainer(userMsg);
        buildTextOutput("The message seems to be empty. Please check the input.");
    } else {
        addMyChatToContainer(userMsg);
        getUserMessage(userMsg);

    }
    $('#txtUserInput').val("");
};

function buildTextOutput(text) {
    var str = "";

    str += '<div class="media media-chat media-chat-reverse"">';
    str += '<div class="media-body">';
    str += '<p>' + text + '</p>';
    // str+='<p>How are you ...???</p>';
    // str+='<p>What are you doing tomorrow?<br> Can we come up a bar?</p>';
    // str+='<p className="meta"></p>';
    str += '</div> </div>';

    $("#chat-content").append(str);
    scrollToBottom();
}

function getUserMessage() {
    //{chatInputs:[{inputText:"string"}]}
    var userMsg = $('#txtUserInput').val();
    jQuery.support.cors = true;
    var allText = [];
    allText.push({facultyId: selFacultyId, courseId: selCourseId, inputText: userMsg});
    // allText.push({courseId: selCourseId});
    // allText.push({inputText: userMsg});

    var chatInputsList = {
        inputText: userMsg,
    }
    var allInput = {
        chatInputs: allText
    };


    $.ajax({
        url: "chatbot/api/v1/response",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(allInput),
        success: function (response) {
            var commonTxt = "";
            var answers = response.answers;
            for (const answer of answers) {
                if (answer.type == 'TEXT') {
                    buildTextOutput(answer.text);
                } else if (answer.type == 'BUTTON') {
                    addButton(answer.inputId, answer.inputId, answer.text, answer.listType);
                    commonTxt = answer.commonText;
                }
            }
            if (commonTxt != '') {
                buildTextOutput(commonTxt);
            }
            console.log(response.answer);
        },
        error: function (xhr, status) {
            alert("Something went wrong.");
        }
    });
}

function addMyChatToContainer(textInput) {

    var str = "";

    str += '<div class="media media-chat">';
    str += '<img class="avatar" src="https://img.icons8.com/color/36/000000/administrator-male.png" alt="...">';
    str += '<div class="media-body">';
    str += '<p>' + textInput + '</p>';
    // str+='<p>How are you ...???</p>';
    // str+='<p>What are you doing tomorrow?<br> Can we come up a bar?</p>';
    // str+='<p className="meta"></p>';
    str += '</div> </div>';

    $("#chat-content").append(str);
    scrollToBottom();
}

/*{"answers":[{"type":"BUTTON","text":"School of Business & Law"},{"type":"BUTTON","text":"School of Management"},
    {"type":"BUTTON","text":"School of Tourism & Hospitality"},
    {"type":"BUTTON","text":"School of Engineering"},{"type":"BUTTON","text":"School of computing"}]}*/
function addButton(id, name, text, listType) {
    var str = "";

    str += '<div class="media media-chat media-chat-reverse"">';
    str += '<div class="media-body">';

    //str += '<p>Click option to select</p>';
    if (listType == 'course_details') {
        str += '<button onclick="updateCourse(' + id + '); " name="' + name + '" id="' + id + '" class="btn btn-success button-list">' + text + '</button>';

    } else {
        str += '<button onclick="updateFaculty(' + id + '); " name="' + name + '" id="' + id + '" class="btn btn-success">' + text + '</button>';
    }


// str+='<p>What are you doing tomorrow?<br> Can we come up a bar?</p>';
// str+='<p className="meta"></p>';
    str += '</div> </div>';

    $("#chat-content").append(str);
    scrollToBottom();

}

function scrollToBottom() {
    $("#chat-content").animate({scrollTop: 20000000}, "slow");
}

function updateFaculty(id) {
    selFacultyId = id;
    getUserMessage();
}

function updateCourse(id) {
    selCourseId = id;
    getUserMessage();
}

