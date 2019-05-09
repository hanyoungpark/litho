/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.facebook.samples.litho.dynamicprops;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentTree;
import com.facebook.litho.DynamicValue;
import com.facebook.litho.LithoView;
import com.facebook.samples.litho.NavigatableDemoActivity;
import com.facebook.samples.litho.R;
import java.util.Calendar;

public class DynamicPropsActivity extends NavigatableDemoActivity
    implements SeekBar.OnSeekBarChangeListener {
  private static final long DAY = 24 * 60 * 60 * 1000;

  private DynamicValue<Long> mTimeValue;

  private TextView mTimeLabel;
  private SeekBar mSeekBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState == null) {
      mTimeValue = new DynamicValue<>(0L);
    }

    setContentView(R.layout.activity_dynamic_props);

    mTimeLabel = findViewById(R.id.time);

    final LithoView lithoView = findViewById(R.id.lithoView);
    final ComponentContext c = new ComponentContext(this);
    final Component component = ClockComponent.create(c).time(mTimeValue).build();
    lithoView.setComponentTree(ComponentTree.create(c, component).build());

    mSeekBar = findViewById(R.id.seekBar);
    mSeekBar.setMax((int) DAY);
    mSeekBar.setOnSeekBarChangeListener(this);

    reset(null);
  }

  private void setTime(long time) {
    mTimeValue.set(time % ClockView.TWELVE_HOURS);
    mTimeLabel.setText(ClockView.getTimeString(time % DAY, false));
    mSeekBar.setProgress((int) time);
  }

  public void reset(View v) {
    final Calendar calendar = Calendar.getInstance();

    final long now = calendar.getTimeInMillis();

    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    final long midnight = calendar.getTimeInMillis();

    final int sinceMidnight = (int) (now - midnight);
    setTime(sinceMidnight);
  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    if (fromUser) {
      setTime(progress);
    }
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {}

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {}
}
