function saveCompanyDetails() {
    let companyDetailsFields = document.querySelectorAll(".company-detail");
    let CompanyData = new Object();

    companyDetailsFields.forEach( (oneElem) => {
        let nextClass = oneElem.classList.item(1);

        switch(nextClass){
            case "name": CompanyData.name = oneElem.value;
            break;
            case "address": CompanyData.address = oneElem.value;
            break;
            case "city": CompanyData.city = oneElem.value;
            break;
            case "phone": CompanyData.phone = oneElem.value;
            break;
            case "email": CompanyData.email = oneElem.value;
            break;
        }
    });

    sendDataToServer(CompanyData);

};

function sendDataToServer(CompanyData) {
        let url = window.location.origin + "/api/account";
        let xhr = new XMLHttpRequest();

        xhr.open("PUT", url, true);
        xhr.setRequestHeader("Content-type", "application/json");

        xhr.onreadystatechange = function() {
            if(xhr.readyState === 4){
                console.log(xhr.status);
                console.log(xhr.responseText);

                if(xhr.status === 200){
                    document.querySelector("#changesSuccessfull").className = "alert alert-success";
                } else
                    document.querySelector("#changesUnsuccessfull").className = "alert alert-danger";
            }
        }

        let data = JSON.stringify(CompanyData);

        xhr.send(data);
};


document.querySelectorAll("#acceptClick").forEach( (t) => {

t.addEventListener("click", function(e) {

e.preventDefault();
//console.log(this.classList.item(0))
acceptReservation(this.classList.item(0));
})
});


document.querySelectorAll("#declineClick").forEach( (t) => {

t.addEventListener("click", function(e) {

e.preventDefault();
//console.log(this.classList.item(0))
acceptReservation(this.classList.item(0));
})
});

function acceptReservation(nd){
    let url = window.location.origin + "/api/reservation/accept";
    let xhr = new XMLHttpRequest();

    xhr.open("PATCH", url, true);
    xhr.setRequestHeader("Content-type", "application/json");

    sendReservationDecision(xhr,nd);
};

function declineReservation(nd){
    let url = window.location.origin + "/api/reservation/delete";
    let xhr = new XMLHttpRequest();

    xhr.open("DELETE", url, true);
    xhr.setRequestHeader("Content-type", "application/json");

    sendReservationDecision(xhr, nd);
};

function sendReservationDecision(xhr,nd){
    xhr.onreadystatechange = function() {
            if(xhr.readyState === 4){
                console.log(xhr.status);
                console.log(xhr.responseText);

                if(xhr.status === 200){

                }
            }
        }
        //console.log("SendReserv: "+nd);

        let reservationId = document.querySelector(".In"+nd).value;
        let reservationCompanyId = document.querySelector(".Ip"+nd).value;
        //console.log("reservId: " + reservationId);
        //console.log("reservCompId: "+reservationCompanyId);

        document.querySelector(".TR"+nd).remove();


        let data = JSON.stringify({
            "id": parseInt(reservationId),
            "companyId": parseInt(reservationCompanyId)
        });

        xhr.send(data);
}