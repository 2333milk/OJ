
<script setup lang="ts">
import {onMounted, ref, watchEffect} from 'vue'
import {useRouter} from 'vue-router';
import {useUserStore} from '@/stores/user'
import ACCESS_ENUM from "@/access/accessEnum";
import MenuRoute from "@/router/MenuRoute";

const router = useRouter();
const selectkeys = ref(['/']);
const userStore = useUserStore();
const menuList = ref();
const loadData = () => {
  menuList.value = MenuRoute.filter((item, index) => {
    if (item?.meta.access !== userStore.user.userRole&&!item?.meta.ordinary) {
      return false;
    }
    return true;
  });
}
watchEffect(() => {
  loadData();
})

onMounted(() => {
  loadData();
})


router.afterEach((to, from, failure) => {
  selectkeys.value = [to.path];
});

const doMenuClick = (key: string) => {
  router.push({
    path: key,
  });
};
const login = () => {
  router.push({
    path: '/user/login',
  });
}

const toPersonCenter = ()=>{
  router.push({
    path: '/list/personCenter',
  });
}

const logout = ()=>{
  userStore.logout();
  router.push({
    path: '/user/login',
  });
}

</script>

<template>
  <!-- 导航栏菜单 -->
  <div id="GlobalHeader">
    <a-row class="grid-demo" style="margin-bottom: 16px;" align="center">
      <a-col flex="100px">
        <div class="title-bar">
          <div class="title">NCHU</div>
          <img class="logo" src="../assets/image.png">
        </div>
      </a-col>
      <a-col flex="auto">
       <a-menu mode="horizontal" :selected-keys="selectkeys" @menu-item-click="doMenuClick">
            <a-menu-item v-for="item in menuList" :key="item?.path">{{ item?.name }}</a-menu-item>
        </a-menu>
      </a-col>
      <a-col flex="100px">
        <a-space size="medium" style="margin-top: 15px;"
                 v-if="userStore.user.userRole == ACCESS_ENUM.NOT_LOGIN">
          <a-button type="outline" @click="login">登录</a-button>
          <!--                    <a-button type="outline" @click="register">注册</a-button>-->
        </a-space>
        <a-popover v-else position="bottom" style="max-width: 140px">
          <a-avatar :style="{ backgroundColor: '#3370ff' }" style="margin-top: 20px">
            {{userStore.user.userName}}
          </a-avatar>
          <template #content>
<!--            <a-button @click="toPersonCenter">-->
<!--              <template #icon>-->
<!--                <icon-skin />-->
<!--              </template>-->
<!--              <template #default>个人信息</template>-->
<!--            </a-button>-->
            <a-button @click="logout">
              <template #icon>
                <icon-export />
              </template>
              <template #default>退出登录</template>
            </a-button>
          </template>
        </a-popover>
      </a-col>
    </a-row>

  </div>
</template>

<style scoped>
#GlobalHeader {
  box-sizing: border-box;
  width: 100%;
}

.title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: #000000;
  margin-left: 16px;
  font-size: 25px;
}

.logo {
  height: 48px;
}
</style>