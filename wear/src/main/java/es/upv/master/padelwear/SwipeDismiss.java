package es.upv.master.padelwear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.SwipeDismissFrameLayout;

/**
 * Created by padres on 16/06/2017.
 */

public class SwipeDismiss extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_dismiss);

        SwipeDismissFrameLayout root = (SwipeDismissFrameLayout) findViewById(R.id.swipe_dismiss_root);
        root.addCallback(new SwipeDismissFrameLayout.Callback() {
            @Override
            public void onDismissed(SwipeDismissFrameLayout layout) {
                SwipeDismiss.this.finish();
            }
        });
    }
}
