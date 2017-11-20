package com.sbi.project.sbi;

import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.ParallaxPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;


public class WelcomeClass extends WelcomeActivity {
    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.colorPrimary)
                .page(new ParallaxPage(R.layout.activity_main,
                        getResources().getString(R.string.titlestring),"")
                )
                .page(new BasicPage(R.drawable.ic_edit_white,
                        "Introduction",getResources().getString(R.string.secondpage))
                        .background(R.color.colorPrimary)
                )
                .page(new BasicPage(R.drawable.ic_view_carousel_white,
                        "Instructions",
                        getResources().getString(R.string.thirdpage))
                ).page(new BasicPage(R.drawable.ic_thumb_up_white,"Features",getResources().getString(R.string.features)))
                .swipeToDismiss(true)
                .build();
    }
}
