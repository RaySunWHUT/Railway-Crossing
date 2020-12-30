<template>
 <div>
     <div class="theme">
      <div class="no">
        404
      </div>
      <p>Page not Found</p>
     </div>
 </div>
</template>
 
<script>
import storage from '@/utils/storage';
export default {

    name: 'NotFound',

    components: {


    },

};

</script>
 
<style lang="less" scoped>

.theme p {
    font-size: 40px;
    font-weight: bold;
    top: 365px;
    left: 610px;
    position: absolute;
}


.no {

    font-size: 180px;
    font-weight: bold;
    top: 180px;
    left: 610px;
    position: absolute;
}

</style>