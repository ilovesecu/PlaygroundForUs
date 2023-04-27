window.openWindow = function (url) {
    const windowName = url.substring(0, url.indexOf("?")) !== '' ? url.substring(0, url.indexOf("?")) : url;
    let winref = "";

    winref = window.open('',windowName,''); //windowName으로 이미 열려있는지 식별가능한듯! -> https://developer.mozilla.org/en-US/docs/Web/API/Window/open#parameters
    if(winref.location.href === 'about:blank'){
        winref.location.href = url;
    }
}