function render(){
	let display = document.getElementById("display");
	let g = ["#00d2ff","#3a7bd5"];
	display.innerHTML = hexPattern(50,800,800,g,1);
}

/*
 * Takes in 
 *   s : size of hexagons in pixels
 *   w : width of pattern
 *   h : height of pattern
 *   g : gradient of hexagons, represented
 *       by an array of 2 strings, start 
 *       and end color, in hex
 *   sp: spacing between hexagpons in pixels
 */
function hexPattern(s,w,h,g,sp){
	let hexH = Math.cos((30/180)*Math.PI)*s;
	let hexW = Math.cos((30/180)*Math.PI)*s;
	let hexs = Math.ceil(w/hexW);
	let lines = Math.ceil(h/hexH);
	return generateSVG(s,hexs,lines,g,sp);
}

function sqr(x){
	return x*x;
}
function generateSVG(s,hexs,lines,gradient,sp){
	let curr = "";
	let x = 0;
	let y = 0;
	let delta = Math.cos((30/180)*Math.PI)*s;
	let yDelta = (s+s/2);
	let hyp = 2*delta;
	let leg = delta+(sp/2);
	let adjust = Math.sqrt(sqr(hyp+sp)-sqr(leg));


	let t = true;

	for (var i = 0; i < lines; i++) {
		curr += generateLine(x,y,hexs,s,gradient,sp);
		if(t)
			x = delta+(sp/2);
		else
			x = 0;

		t = !t;
		y += adjust;
	}
	return curr;
}

function generateLine(sx,sy,hexs,s,gradient,sp){
	let x = sx;
	let ret = "";
	let col = "";
	let t = true;
	let delta = Math.cos((30/180)*Math.PI)*s;
	for (var i = 0; i < hexs; i++) {

		col = getColor((i/(hexs-1)),gradient);

		ret += hexagon(x,sy,s,col);
		x += (2*delta+sp);
	}
	return ret;
}

function hexagon(cx,cy,s,col){
	let ret = '<polygon points="';
	let x = 0;
	let y = 0;
	for (var angle = 30; angle < 360; angle+=60) {
		x = Math.round(s*Math.cos((angle/180)*Math.PI));
		y = Math.round(s*Math.sin((angle/180)*Math.PI));
		ret += point(cx+x,cy+y);
	}


	ret += '" style="stroke-width:1;fill:'+col+'" />';
	return ret;
}

function point(x,y){
	return (x+","+y+" ");
}

/*
 * Color functions
 */

function normalise(a){
	if(a < 0)
		return 360+a;
	if(a>360)
		return a%360;
	return a;
}
function circularInterpol(x,y,t){
	// 191 -> 215
	if (y >= x && y-x <= 180) {
		return normalise(x+(y-x)*t);
	} else if (y >= x && y-x > 180) {
		return normalise(x-t*(360-(y-x)));
	} else if (x > y && x-y <= 180) {
		return normalise(x-t*(x-y));
	} else if (x > y && x-y > 180) {
		return normalise(x+t*(360-(x-y)));
	}
}

function getColor(num,gradient) {
	let start = hextoHSV(gradient[0]);
	let end = hextoHSV(gradient[1]);
	
	let h = circularInterpol(start[0],end[0],num);
	let s = start[1]+((end[1]-start[1]))*num;
	let v = start[2]+((end[2]-start[2]))*num;
	return HSVtoHex(h,s,v);
}

function mod(a,n){
    return ((a%n)+n)%n;
};

function calcHue(r,g,b,m,d){
	if(d == 0) {
		res = 0;
	}else if(m == r){
		res = 60*(mod(((g-b).toFixed(2)/d),6));
	}else if(m == g){
		res = 60*(((b-r).toFixed(2)/d)+2);
	}else if(m == b){
		res = 60*(((r-g).toFixed(2)/d)+4);
	}else{
		console.log("SHITS FUCKED YO: "+r+","+g+","+b+" : "+m);
	}
	if (res < 0)
		console.log("H IS NEGATIVE???: "+(r*255)+","+(g*255)+","+(b*255)+" : "+m);
	return res;
}

function calcSat(d,m){
	if(m == 0) {
		return 0;
	}else{
		return d/m;
	}
}	

function hextoHSV(hex){
	let red = parseInt(hex.substring(1,3),16)/255;
	let green = parseInt(hex.substring(3,5),16)/255;
	let blue = parseInt(hex.substring(5,7),16)/255;
	let cMax = Math.max(red,green,blue);
	let cMin = Math.min(red,green,blue);
	let delta = cMax-cMin;
	let value = cMax;
	let hue = calcHue(red,green,blue,cMax,delta);
	let sat = calcSat(delta, cMax);
	console.log([hue,sat,value]);
	return [hue,sat,value];
}


function collapseHue(h,c,x){
	if(h >= 0 && h <60) {
		return [c,x,0];
	}else if(h>=60 && h <120){
		return [x,c,0];
	}else if(h>=120 && h <180){
		return [0,c,x];
	}else if(h>=180 && h <240){
		return [0,x,c];
	}else if(h>=240 && h <300){
		return [x,0,c];
	}else if(h>=300 && h < 360){
		return [c,0,x];
	}else{
		console.log("SHITS FUCKED YO: "+h);
	}
}

function HSVtoHex(h,s,v){
	let c = v*s;
	let x = c*(1-Math.abs(((h/60)%2)-1));
	let m = v-c;
	let raw = collapseHue(h,c,x);
	let r = Math.floor((raw[0]+m)*255);
	let g = Math.floor((raw[1]+m)*255);
	let b = Math.floor((raw[2]+m)*255);
	return rgbToHex(r,g,b);
}


// stolen from 
// https://stackoverflow.com/questions/5623838/rgb-to-hex-and-hex-to-rgb
function componentToHex(c) {
    var hex = c.toString(16);
    return hex.length == 1 ? "0" + hex : hex;
}

function rgbToHex(r, g, b) {
    return "#" + componentToHex(r) + componentToHex(g) + componentToHex(b);
}

