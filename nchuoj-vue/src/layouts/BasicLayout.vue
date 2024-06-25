<script setup lang="ts">
import { RouterView } from 'vue-router';
import GlobalHeader from '@/components/GlobalHeader.vue';
</script>

<template>
    <div id="basicLayout">
        <a-layout style="min-height: 100vh;width: 100%;" >
            <a-layout-header class="header">
                <GlobalHeader />
            </a-layout-header>
            <a-layout-content class="context">
                <RouterView />
            </a-layout-content>
            <a-layout-footer class="footer">南昌航空大学OJ判题系统</a-layout-footer>
        </a-layout>
        
    </div>
</template>
<style scoped>
#basicLayout{
    padding:0px;
    margin:0px;
    width: 100%;    
}

#basicLayout .header{
    box-shadow:#444 1px 1px 5px;
}
#basicLayout .context{
    padding: 20px;
}
#basicLayout .footer{
    position:sticky;
    bottom:0;
    left:0;
    right:0;
    text-align:center;
}
</style>