<script setup lang="ts">
import { reactive } from 'vue';
import { UserControllerService, type UserRegisterRequest } from '@/api';
import { useUserStore } from '@/stores/user';
import { Message } from '@arco-design/web-vue';
import { useRouter } from 'vue-router';
const form = reactive({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
} as UserRegisterRequest);
const router = useRouter();
const userStore = useUserStore();
const handleSubmit = async () => {
  const res = await UserControllerService.userRegisterUsingPost(form);
  //登录成功跳转主页
  if (res.code === 0) {
    await userStore.login();
    router.push({
      path: '/user/login',
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
  checkPassword: [
    {
      required: true,
      message: '未再次输入密码',
    }
  ]
}
</script>

<template>
  <div id="userRegister">
    <div class="form-card">
      <h2>用户注册</h2>
      <a-form :model="form" style="max-width: 480px;margin: auto;" :rules="rules" @submit-success="handleSubmit">
        <a-form-item field="userAccount" label="账号">
          <a-input v-model="form.userAccount" placeholder="请输入账号" />
        </a-form-item>
        <a-form-item field="userPassword" label="密码">
          <a-input-password v-model="form.userPassword" placeholder="请输入密码" />
        </a-form-item>
        <a-form-item field="checkPassword" label="校验密码">
          <a-input-password v-model="form.checkPassword" placeholder="请再次输入密码" />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit-success" long>注册</a-button>
        </a-form-item>
        <a-form-item>
          <a-link href="/user/login">登录</a-link>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<style scoped>
.form-card{
  margin-top: 15%;
}
</style>