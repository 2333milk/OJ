<script setup lang="ts">
import { RouterView } from 'vue-router';
</script>

<template>
    <div id="userLayout">
        <a-layout style="min-height: 100vh;width: 100%;">
            <a-layout-header class="header">
                <div class="title-bar">
                    <a-space>
                        <div class="title">NCHU</div>
                        <img class="logo" src="../assets/image.png">
                    </a-space>
                </div>
            </a-layout-header>
            <a-layout-content class="context">
                <RouterView />
            </a-layout-content>
            <a-layout-footer class="footer">南昌航空大学OJ判题系统----by-212015班-张丽星</a-layout-footer>
        </a-layout>

    </div>
</template>

<style scoped>
#userLayout {
    background: url(&quot;https://gw.alipayobjects.com/zos/rmsportal/FfdJeJRQWjEeGTpqgBKj.png&quot;) 0% 0% / 100% 100%;
    text-align: center;
    display: flex;
}

#userLayout .title-bar {
    align-items: center;
}

#userLayout .logo {
    height: 48px;
}

#userLayout .title {
    color: #000000;
    margin-left: 16px;
    font-size: 25px;
}

#userLayout .header {
    padding: 20px;
}

#userLayout .context {
    padding: 20px;
}

#userLayout .footer {
    position: sticky;
    bottom: 0;
    left: 0;
    right: 0;
    text-align: center;
}
</style>