let allPhotos = document.querySelectorAll(".img-thumbnail");
let nmb=0;

allPhotos.forEach( (oneImg) => {
    oneImg.setAttribute("id", nmb)
    oneImg.setAttribute("onclick", "openGallery('"+nmb+"')");
    oneImg.style.cssText= "cursor: pointer";
    nmb++;
});

function openGallery(imgClickedId){
    let currentImg = document.querySelector("div.grid-item img[id='"+imgClickedId+"']");
    let imgUrl = currentImg.getAttribute("src");

    let container = document.body;
    let newImgWindow = document.createElement("div");
    container.appendChild(newImgWindow);
    newImgWindow.setAttribute("class", "img-window");
    newImgWindow.setAttribute("onclick", "closeImg()");

    let newImg = document.createElement("img");
    newImgWindow.appendChild(newImg);

    newImg.setAttribute("src", imgUrl);
    newImg.setAttribute("id", "galleryOpenedImg");

    openImage(newImg, container, currentImg);

};

function openImage(newImg, container, currentImg) {
    //console.log("openImage: " + currentImg.getAttribute("id"));

    newImg.onload = function () {
        let imgWidth = this.width;

        let windowWidth = window.innerWidth;
        let calcImgToEdge = ((windowWidth - imgWidth) / 2) - 80;


    let newPrevBtn = document.createElement("a");
    let newPrevBtnText = document.createTextNode("Prev");
    newPrevBtn.appendChild(newPrevBtnText);
    container.appendChild(newPrevBtn);

    newPrevBtn.setAttribute("class", "img-btn-prev");
    newPrevBtn.setAttribute("onclick", "changeImg('"+currentImg.getAttribute("id")+"', 'prev')");
    newPrevBtn.style.cssText = "left: "+ calcImgToEdge +"px;";

    let newNextBtn = document.createElement("a");
    let newNextBtnText = document.createTextNode("Next");
    newNextBtn.appendChild(newNextBtnText);
    container.appendChild(newNextBtn);

    newNextBtn.setAttribute("class", "img-btn-next");
    newNextBtn.setAttribute("onclick", "changeImg('"+currentImg.getAttribute("id")+"', 'next')");
    newNextBtn.style.cssText = "right: "+ calcImgToEdge +"px;";

    }}

function changeImg(imgId, direction){
    document.querySelector(".img-btn-next").remove();
    document.querySelector(".img-btn-prev").remove();

    let imgSelector = document.querySelector("#galleryOpenedImg");
    let currentImg;

    if(direction == "prev"){
        let prevImg = getPrevImg(parseInt(imgId));
        //console.log("changeImg: " + prevImg);
        imgSelector.setAttribute("src", allPhotos[prevImg].getAttribute("src"));
        currentImg = document.querySelector("div.grid-item img[id='"+prevImg+"']");
    }
    else {
        let nextImg = getNextImg(parseInt(imgId));
        //console.log("changeImg: " + nextImg);
        imgSelector.setAttribute("src", allPhotos[nextImg].getAttribute("src"));
        currentImg = document.querySelector("div.grid-item img[id='"+nextImg+"']");
    }

    openImage(imgSelector, document.body, currentImg);
};

function getPrevImg(currentImgId){
    //console.log("getPrevImg: " + currentImgId);
    if(currentImgId===0)
        return allPhotos.length - 1;
    else
        return currentImgId - 1;
};

function getNextImg(currentImgId){
    //console.log("getNextImg: " + currentImgId);
    let endImg = allPhotos.length -1;
    if(currentImgId===endImg)
        return 0;
    else
        return currentImgId + 1;
};

function closeImg(){
    document.querySelector(".img-window").remove();
    document.querySelector(".img-btn-next").remove();
    document.querySelector(".img-btn-prev").remove();
};