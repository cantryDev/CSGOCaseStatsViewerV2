let scripts = document.getElementsByTagName("script");
const cursorRegex = /g_historyCursor = ([^;]+);/;
let found = false;
for (let i = 0; i < scripts.length; i++) {
    let currentScript = scripts[i];
    if(currentScript.text.includes("g_historyCursor")){
        let regexResult = cursorRegex.exec(currentScript.text);
        if(regexResult.length === 2){
            console.log("Cursor found: ")
            console.log(regexResult[1]);
            found = true;
            break;
        }
    }
}
if(!found){
    console.log("Failed to find cursor. Are you logged in?")
}