<script setup lang="ts">
import { reactive } from 'vue';
import { UserControllerService, type UserLoginRequest } from '@/api';
import { useUserStore } from '@/stores/user';
import { Message } from '@arco-design/web-vue';
import { useRouter } from 'vue-router';
const form = reactive({
  userAccount: '',
  userPassword: '',
} as UserLoginRequest);
const router = useRouter();
const userStore = useUserStore();
const handleSubmit = async () => {
  const res = await UserControllerService.userLoginUsingPost(form);
  //登录成功跳转主页
  if (res.code === 0) {
    await userStore.login();
    router.push({
      path: '/',
      replace: true,
    })
  } else {
    Message.error("登录失败." + res.message);
  }

};
const rules = {
  userAccount: [
    {
      required: true,
      message: '未输入账号',
    },
  ],
  userPassword: [
    {
      required: true,
      message: '未输入密码',
    },
  ],
}
</script>

<template>
  <div id="userLogin">
    <h2>用户登录</h2>
    <a-form :model="form" style="max-width: 480px;margin: auto;" @submit="handleSubmit" :rules="rules" @submit-success="handleSubmit">
      <a-form-item field="userAccount" label="账号">
        <a-input v-model="form.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item field="userPassword" label="密码">
        <a-input-password v-model="form.userPassword" placeholder="请输入密码" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit-success" long>登录</a-button>
      </a-form-item>
      <a-form-item>
        <a-link href="/user/register">注册</a-link>
      </a-form-item>
    </a-form>
  </div>

</template>
<style scoped></style>