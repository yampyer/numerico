package co.edu.eafit.numerico;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import models.Method;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

  private List<Method> methodsList;

  public static class HistoryViewHolder extends RecyclerView.ViewHolder {
    CardView cv;
    TextView methodName;
    TextView functionName;
    TextView secondFunctionName;
    TextView thirdFunctionName;
    TextView date;

    HistoryViewHolder(View itemView) {
      super(itemView);
      cv = (CardView) itemView.findViewById(R.id.cv);
      methodName = (TextView) itemView.findViewById(R.id.method);
      functionName = (TextView) itemView.findViewById(R.id.function);
      secondFunctionName = (TextView) itemView.findViewById(R.id.secondFunction);
      thirdFunctionName = (TextView) itemView.findViewById(R.id.thirdFunction);
      date = (TextView) itemView.findViewById(R.id.date);
    }
  }

  public HistoryAdapter(List<Method> methodsList) {
    this.methodsList = methodsList;
  }

  @Override
  public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.list_item_history, parent, false);

    return new HistoryViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(HistoryViewHolder holder, int position) {
    Method method = methodsList.get(position);
    holder.methodName.setText(method.getMethod());
    holder.functionName.setText(method.getFunction());
    holder.secondFunctionName.setText(method.getSecondFunction());
    holder.thirdFunctionName.setText(method.getThirdFunction());
    holder.date.setText(method.getDate());
  }

  @Override
  public int getItemCount() {
    return methodsList.size();
  }

}
