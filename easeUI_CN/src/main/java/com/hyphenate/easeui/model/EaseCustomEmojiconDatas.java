package com.hyphenate.easeui.model;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojicon.Type;
import com.hyphenate.easeui.utils.EaseSmileUtils;

public class EaseCustomEmojiconDatas {
    
    private static String[] emojis = new String[]{
        EaseSmileUtils.custom_ee_1,
        EaseSmileUtils.custom_ee_2,
        EaseSmileUtils.custom_ee_3,
        EaseSmileUtils.custom_ee_4
    };
    
    private static int[] icons = new int[]{
        R.drawable.home_fefurbish,
        R.drawable.home_fb,
        R.drawable.home_rw,
        R.drawable.home_xx
    };
    
    
    private static final EaseEmojicon[] DATA = createData();
    
    private static EaseEmojicon[] createData(){
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for(int i = 0; i < icons.length; i++){
            datas[i] = new EaseEmojicon(icons[i], emojis[i], Type.NORMAL);
        }
        return datas;
    }
    
    public static EaseEmojicon[] getData(){
        return DATA;
    }
}
