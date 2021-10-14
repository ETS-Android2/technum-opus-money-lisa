package com.technumopus.moneylisa.controller.help;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.controller.home.HomeFragment;


//Adding for help

public class helpfragment extends Fragment implements View.OnClickListener {

    RelativeLayout relativeLayout, relativeLayout1, relativeLayout2, relativeLayout3, relativeLayout4, relativeLayout5;
    Button viewmore, viewmore1, viewmore2, viewmore3, viewmore4, viewmore5;
    int height, height1, height2, height3, height4, height5;
    private HomeFragment.OnFragmentItemSelectedListener listener;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Help");

        View root = inflater.inflate(R.layout.fragment_help, container, false);


        relativeLayout = (RelativeLayout) root.findViewById(R.id.expandable);
        relativeLayout1 = (RelativeLayout) root.findViewById(R.id.expandable1);
        relativeLayout2 = (RelativeLayout) root.findViewById(R.id.expandable2);
        relativeLayout3 = (RelativeLayout) root.findViewById(R.id.expandable3);
        relativeLayout4 = (RelativeLayout) root.findViewById(R.id.expandable4);
        relativeLayout5 = (RelativeLayout) root.findViewById(R.id.expandable5);

        viewmore = (Button) root.findViewById(R.id.viewmore);
        viewmore1 = (Button) root.findViewById(R.id.viewmore1);
        viewmore2 = (Button) root.findViewById(R.id.viewmore2);
        viewmore3 = (Button) root.findViewById(R.id.viewmore3);
        viewmore4 = (Button) root.findViewById(R.id.viewmore4);
        viewmore5 = (Button) root.findViewById(R.id.viewmore5);

        viewmore.setOnClickListener((View.OnClickListener) this);
        viewmore1.setOnClickListener((View.OnClickListener) this);
        viewmore2.setOnClickListener((View.OnClickListener) this);
        viewmore3.setOnClickListener((View.OnClickListener) this);
        viewmore4.setOnClickListener((View.OnClickListener) this);
        viewmore5.setOnClickListener((View.OnClickListener) this);

        relativeLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        relativeLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        relativeLayout.setVisibility(View.GONE);
                        relativeLayout1.setVisibility(View.GONE);
                        relativeLayout2.setVisibility(View.GONE);
                        relativeLayout3.setVisibility(View.GONE);
                        relativeLayout4.setVisibility(View.GONE);
                        relativeLayout5.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        relativeLayout.measure(widthSpec, heightSpec);
                        height = relativeLayout.getMeasuredHeight();
                        relativeLayout1.measure(widthSpec, heightSpec);
                        height1 = relativeLayout.getMeasuredHeight();
                        relativeLayout2.measure(widthSpec, heightSpec);
                        height2 = relativeLayout.getMeasuredHeight();
                        relativeLayout3.measure(widthSpec, heightSpec);
                        height3 = relativeLayout.getMeasuredHeight();
                        relativeLayout4.measure(widthSpec, heightSpec);
                        height4 = relativeLayout.getMeasuredHeight();
                        relativeLayout5.measure(widthSpec, heightSpec);
                        height5 = relativeLayout.getMeasuredHeight();
                        return true;
                    }
                });
        return root;

    }

    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof HomeFragment.OnFragmentItemSelectedListener){
            listener = (HomeFragment.OnFragmentItemSelectedListener) context;

        }else {
            throw new ClassCastException(context.toString() + " must implement listener");
        }
    }
    private void expand(RelativeLayout layout, int layoutHeight) {
        layout.setVisibility(View.VISIBLE);
        ValueAnimator animator = slideAnimator(layout, 0, layoutHeight);
        animator.start();
    }

    private void collapse(final RelativeLayout layout) {
        int finalHeight = layout.getHeight();
        ValueAnimator mAnimator = slideAnimator(layout, finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator(final RelativeLayout layout, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
                layoutParams.height = value;
                layout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewmore:
                if (relativeLayout.getVisibility() == View.GONE) {
                    expand(relativeLayout, height);
                } else {
                    collapse(relativeLayout);
                }
                break;

            case R.id.viewmore1:
                if (relativeLayout1.getVisibility() == View.GONE) {
                    expand(relativeLayout1, height1);
                } else {
                    collapse(relativeLayout1);
                }
                break;

            case R.id.viewmore2:
                if (relativeLayout2.getVisibility() == View.GONE) {
                    expand(relativeLayout2, height2);
                } else {
                    collapse(relativeLayout2);
                }
                break;

            case R.id.viewmore3:
                if (relativeLayout3.getVisibility() == View.GONE) {
                    expand(relativeLayout3, height3);
                } else {
                    collapse(relativeLayout3);
                }
                break;

            case R.id.viewmore4:
                if (relativeLayout4.getVisibility() == View.GONE) {
                    expand(relativeLayout4, height4);
                } else {
                    collapse(relativeLayout4);
                }
                break;

            case R.id.viewmore5:
                if (relativeLayout5.getVisibility() == View.GONE) {
                    expand(relativeLayout5, height5);
                } else {
                    collapse(relativeLayout5);
                }
                break;
        }
    }
    public interface  OnFragmentItemSelectedListener{
        public void onButtonSelectedHome(int id);
    }
}
