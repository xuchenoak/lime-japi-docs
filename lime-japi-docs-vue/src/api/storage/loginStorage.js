
// 是否登录状态
import store from "@/util/store";

export function isLogin() {
  const account = sessionStorage.getItem("account")
  const password = sessionStorage.getItem("password")
  if (account == null || account.trim().length == 0) {
    return false
  }
  if (password == null || password.trim().length == 0) {
    return false
  }
  return true
}

// 获取账号密码
export function get() {
  return {
    account: sessionStorage.getItem("account"),
    password: sessionStorage.getItem("password")
  }
}

// 登录
export function login(account, password) {
  sessionStorage.setItem("account", account)
  sessionStorage.setItem("password", password)
  return new Promise(resolve => {
    store.dispatch("common").then(res => {
      resolve(res)
    })
  })
}

// 登出
export function logout() {
  sessionStorage.removeItem("account")
  sessionStorage.removeItem("password")
  return new Promise(resolve => {
    store.dispatch("common").then(res => {
      resolve(res)
    })
  })
}