
import router from '@/router';
import { useUserStore } from '@/stores/user';
import accessEnum from "@/access/accessEnum";


router.beforeEach(async(to,from,next)=>{
    const useStore = useUserStore();
    const user = useStore.user;
    //若未登录过则登录
    if(to.meta.needLogin){
        if(user.userRole===accessEnum.NOT_LOGIN){
            next('/user/login');
            return;
        }else {
            if(!to.meta.ordinary&&to.meta.access!==user.userRole) {
                next('/noAuth');
                return;
            }
        }

    }
    next();
})