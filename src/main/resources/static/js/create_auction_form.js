$('.ui.dropdown').dropdown();
$(function () {
    // Turn input element into a pond
    $('.my-pond').filepond();

    // Set allowMultiple property to true
    $('.my-pond').filepond('allowMultiple', true);

    // Listen for addfile event
    $('.my-pond').on('FilePond:addfile', function (e) {
        console.log('file added event', e);
    });


});

$(document).ready(function () {
    let today = new Date().toISOString().slice(0, 10);
    $('#date-input').attr('min', today)
    $('#date-input').attr('value', today)
})

var dateInput = document.getElementById("date-input");
var getDateTime = document.getElementById("date-end");
var result = document.getElementById("output-show");

Date.prototype.addDays = function (days) {
    var date = new Date(this.valueOf());
    date.setDate(date.getDate() + days);
    return date;
}

function showdate() {
    var newDate = new Date(dateInput.value);
    if (getDateTime.value == null)
        result.innerHTML = newDate.getDate() + '-' + (newDate.getMonth() + 1) + '-' + newDate.getFullYear();
    else
        showResult()
}

function showDateTime() {
    var output = dateInput.value;
    result.innerHTML = output;
}

function dateOutput(date, days) {
    var newDate = new Date(date);
    var result = newDate.addDays(Number(days));
    var date = result.getDate() + '-' + (result.getMonth() + 1) + '-' + result.getFullYear();
    return date;
}

function showResult() {
    var output = dateOutput(dateInput.value, getDateTime.value);
    result.innerHTML = output;
}