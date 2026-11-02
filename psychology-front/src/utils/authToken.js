//存，取，删

import Cookies from 'js-cookie';
const key = "psychology"

function setToken(token){
    Cookies.set(key,token)
}

function getToken(){
    return Cookies.get(key)
}

function removeToken(){
    Cookies.remove(key)
}

export default{setToken,getToken,removeToken}
