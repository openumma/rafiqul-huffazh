package org.tangaya.quranasrclient.murojaah.evaldetail;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.tangaya.quranasrclient.R;
import org.tangaya.quranasrclient.databinding.EvalCardBinding;

import java.util.ArrayList;

public class EvalAdapter extends RecyclerView.Adapter<EvalAdapter.MyViewHolder> {

    private ArrayList<EvalDetailViewModel> mArrayList;

    private Context mContext;

    private LayoutInflater layoutInflater;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView verseNum, rec, ref, diff, eval;
        ImageView evaluationIcon;

        private EvalCardBinding mBinding;

        public MyViewHolder(EvalCardBinding binding) {
            super(binding.getRoot());

            mBinding = binding;

            verseNum = itemView.findViewById(R.id.verse_num_evaluation__card);
            rec = itemView.findViewById(R.id.verse_rec_evaluation__card);
            ref = itemView.findViewById(R.id.verse_ref_evaluation__card);
            diff = itemView.findViewById(R.id.verse_diff_evaluation__card);
            evaluationIcon = itemView.findViewById(R.id.evaluation_icon__card);
            eval = itemView.findViewById(R.id.eval__card);
        }

        public void bind(EvalDetailViewModel viewModel) {
            mBinding.setViewmodel(viewModel);
            mBinding.executePendingBindings();
        }

        public EvalCardBinding getBinding() {
            return mBinding;
        }
    }

    public EvalAdapter(Context context, ArrayList<EvalDetailViewModel> arrayList) {
        mContext = context;
        mArrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        EvalCardBinding evalBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.card_evaluation, parent, false);

        return new MyViewHolder(evalBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        EvalDetailViewModel mViewModel = mArrayList.get(position);
        holder.bind(mViewModel);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


}
