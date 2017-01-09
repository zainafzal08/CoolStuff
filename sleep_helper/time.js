function pad(num){
  if(parseInt(num) < 10){
    return "0" + num;
  }else{
    return num;
  }
}
function getDate(){
  setTimeout(function() {
    var currentdate = new Date();
    /* calculate hours of sleep */
    var remain = 0
    if (parseInt(currentdate.getHours()) < 7){
      remainH =  7 - parseInt(currentdate.getHours())
    } else{
      remainH =  24 - parseInt(currentdate.getHours())
      remainH = remain + 7
    }
    if (parseInt(currentdate.getMinutes()) == 0){
      remainM =  "0"
    } else{
      remainH =  remainH -1
      remainM = 60 - parseInt(currentdate.getMinutes())
    }
    /* get current time */
    if(currentdate.getHours() == '0'){
      var hours = 12;
      var suffix = "AM"
    }
    else if(parseInt(currentdate.getHours()) >  12 ){
      var hours = parseInt(currentdate.getHours()) - 12
      var suffix = "PM"
    }
    else if (currentdate.getHours() == "12"){
      var hours = 12;
      var suffix = "PM"
    }else{
      var hours = currentdate.getHours();
      var suffix = "AM"
    }
    var time = hours + ":" + pad(currentdate.getMinutes()) + ":" + pad(currentdate.getSeconds()) + " " + suffix
    document.getElementById("time").innerHTML = time
    document.getElementById("warn").innerHTML = "If you sleep right now you get " + remainH + " hours and " + remainM +" Minutes of sleep"
    getDate()
  }, 1000)
}
getDate()
