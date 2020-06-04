package com.haotang.pet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.NoteTagAdapter;
import com.haotang.pet.entity.NoteTag;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.GridSpacingItemDecoration;
import com.haotang.pet.view.NoScollFullGridLayoutManager;
import com.umeng.analytics.MobclickAgent;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单备注界面
 */
public class NoteNewActivity extends SuperActivity {
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.tv_titlebar_other)
    TextView tvTitlebarOther;
    @BindView(R.id.et_note_content)
    EditText etNoteContent;
    @BindView(R.id.rv_note)
    RecyclerView rvNote;
    @BindView(R.id.rl_note)
    RelativeLayout rlNote;
    @BindView(R.id.bt_note_submite)
    Button btNoteSubmite;
    @BindView(R.id.showtext)
    TextView showtext;
    private String remark;
    private List<NoteTag> list;
    private NoteTagAdapter noteTagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findView();
        setView();
        setLinster();
    }

    private void initData() {
        list = (List<NoteTag>) getIntent().getSerializableExtra("list");
        remark = getIntent().getStringExtra("remark");
    }

    private void findView() {
        setContentView(R.layout.activity_note_new);
        ButterKnife.bind(this);
    }

    private void setView() {
        tvTitlebarTitle.setText("订单备注");
        rlNote.setVisibility(View.VISIBLE);
        btNoteSubmite.setVisibility(View.GONE);
        tvTitlebarOther.setVisibility(View.VISIBLE);
        tvTitlebarOther.setText("保存");
        tvTitlebarOther.setTextColor(Color.parseColor("#FF384359"));
        rvNote.setHasFixedSize(true);
        rvNote.setNestedScrollingEnabled(false);
        NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                NoScollFullGridLayoutManager(rvNote, mContext, 2, GridLayoutManager.VERTICAL, false);
        noScollFullGridLayoutManager.setScrollEnabled(false);
        rvNote.setLayoutManager(noScollFullGridLayoutManager);
        rvNote.addItemDecoration(new GridSpacingItemDecoration(2, getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                true));
        noteTagAdapter = new NoteTagAdapter(R.layout.item_notetag, list);
        rvNote.setAdapter(noteTagAdapter);
        if (Utils.isStrNull(remark)) {
            etNoteContent.setText(remark);
            CharSequence text = etNoteContent.getText();
            if (text instanceof Spannable) {
                Spannable spantext = (Spannable) text;
                Selection.setSelection(spantext, text.length());
            }
        }
    }

    private void setLinster() {
        etNoteContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() == 100) {
                    ToastUtil.showToastShortCenter(mContext,
                            "最多输入100字");
                } else {
                    showtext.setText(s.length() + "/100");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        if (noteTagAdapter != null) {
            noteTagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    list.get(position).setSelected(!list.get(position).isSelected());
                    adapter.notifyDataSetChanged();
                }
            });
        }
        etNoteContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && Utils.isStrNull(s.toString()) && Utils.isStrNull(s.toString().trim()) &&
                        s.toString().trim().length() >= 100) {
                    ToastUtil.showToastShortBottom(mContext, "字数不能超过100");
                }
            }
        });
    }

    @OnClick({R.id.ib_titlebar_back, R.id.tv_titlebar_other, R.id.bt_note_submite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_titlebar_back:
                finish();
                break;
            case R.id.tv_titlebar_other:
                submiteNote();
                break;
            case R.id.bt_note_submite:
                submiteNote();
                break;
        }
    }

    private void submiteNote() {
        String content = etNoteContent.getText().toString().trim();
        Intent data = new Intent();
        if (list != null && list.size() > 0) {
            boolean isSelected = false;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isSelected()) {
                    isSelected = true;
                    break;
                }
            }
            if (!Utils.isStrNull(content) && !isSelected) {
                ToastUtil.showToastShort(this, "请输入备注信息或者选择标签");
                return;
            }
            data.putExtra("list", (Serializable) list);
        } else {
            if (!Utils.isStrNull(content)) {
                ToastUtil.showToastShort(this, "请输入备注信息");
                return;
            }
        }
        data.putExtra("note", content);
        setResult(Global.RESULT_OK, data);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("NoteActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("NoteActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
        MobclickAgent.onPause(this);
    }
}
