document.querySelector("#btnReservation").addEventListener("click", function(e) {
e.preventDefault();
sendReservation();
});

function sendReservation() {
    let startDate = document.querySelector("#input-val13").value;
    //console.log(startDate);
    let startTime = document.querySelector("#input-val14").value;
    //console.log(startTime);

    let endDate = document.querySelector("#input-val15").value;
    //console.log(endDate);
    let endTime = document.querySelector("#input-val16").value;
    //console.log(endTime);

    sendPostReservation(startDate, startTime, endDate, endTime);

};

function sendPostReservation(startDate, startTime, endDate, endTime){
    let url = window.location.origin + "/api/reservation/create";
    let xhr = new XMLHttpRequest();

    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-type", "application/json");

    xhr.onreadystatechange = function() {
        if(xhr.readyState === 4){
            console.log(xhr.status);
            console.log(xhr.responseText);

            if(xhr.status === 200){
                document.querySelector("#reservationSuccessfull").className = "alert alert-success";
            }
        }
    }

    let carId = document.querySelector("#input-valCarId").value;
    let companyId = document.querySelector("#input-valCompanyId").value;

    let data = JSON.stringify({
        "startDate": startDate,
        "startTime": startTime,
        "endDate": endDate,
        "endTime": endTime,
        "carId": parseInt(carId),
        "companyId": parseInt(companyId)
    });

    xhr.send(data);
};
