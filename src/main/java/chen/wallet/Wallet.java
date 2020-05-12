package chen.wallet;

import chen.dao.BlockChain;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author AChen
 * @Data: 2020/5/11 11:31 下午
 */
@Data
public class Wallet {


/*    public void addNewMoney(String uuid,Integer amount){

            hashMap.put(uuid,amount);

        }*/

    BlockChain blockChain = BlockChain.getInstance();

    //减少余额
    public Boolean delMoney(String uuid,Integer amount){
        if (blockChain.hashMap.containsKey(uuid) && blockChain.hashMap.get(uuid)>=amount){
            Integer remove = blockChain.hashMap.remove(uuid);
            blockChain.hashMap.put(uuid,remove-amount);
            return true;
        }
        else return false;
    }




    //增加个人余额
    public void addMoneyToP(String uuid,Integer amount){
if (blockChain.hashMap.containsKey(uuid)){
            Integer remove = blockChain.hashMap.remove(uuid);
            blockChain.hashMap.put(uuid,remove+amount);}
else {
    blockChain.hashMap.put(uuid,amount);
}
        }


    //检余额
    public Boolean checkMoney(String senderAddress,Integer amount) {
        if (blockChain.hashMap.containsKey(senderAddress) && blockChain.hashMap.get(senderAddress)>=amount){
            return true;
        }else {
            return false;
        }
    }
}
