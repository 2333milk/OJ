import { ref} from 'vue'
import { defineStore } from 'pinia'
import {UserControllerService} from '@/api'
import ACCESS_ENUM from '@/access/accessEnum'
import {Message} from "@arco-design/web-vue";

export const useUserStore = defineStore('user', () => {
  const user = ref({
    userName:"",
    userRole:ACCESS_ENUM.NOT_LOGIN,
  })

  function changeUser(name:any,role:any){
    user.value.userName = name;
    user.value.userRole = role;
  }

  async function login() {
    const res = await UserControllerService.getLoginUserUsingGet();
    if(res.code === 0){
        changeUser(res.data?.userName,res.data?.userRole);
    }else {
      reSet();
    }
  }
  function reSet(){
    user.value.userName = "";
    user.value.userRole = ACCESS_ENUM.NOT_LOGIN;
  }
  async function logout(){
    const res = await UserControllerService.userLogoutUsingPost();
    if(res.code === 0) {
      Message.success("登出成功");
    }reSet();
  }
  return { user,changeUser, login,logout}
},{
  persist: true,
},)
