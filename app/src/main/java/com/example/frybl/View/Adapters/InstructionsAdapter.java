package com.example.frybl.View.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frybl.Model.Instruction;
import com.example.frybl.R;

import java.util.ArrayList;
import java.util.List;

public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsAdapter.InstructionHolder> {

    private List<Instruction> instructionList;

    public InstructionsAdapter()
    {
        instructionList = new ArrayList<>();
    }

    @NonNull
    @Override
    public InstructionsAdapter.InstructionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_add_instructions_item,parent,false);
        return new InstructionsAdapter.InstructionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionsAdapter.InstructionHolder holder, int position) {
        Instruction currentInstruction = instructionList.get(position);
        holder.instructionItemStepText.setText(currentInstruction.getDescription());
        holder.instructionItemStepNumber.setText(String.valueOf(position + 1));

    }

    @Override
    public int getItemCount() {
        return instructionList.size();
    }

    public Instruction getInstructionAt(int position)
    {
        return instructionList.get(position);
    }

    public void setInstructionList(List<Instruction> instructionList)
    {
        this.instructionList = instructionList;
        notifyDataSetChanged();
    }


    class InstructionHolder extends RecyclerView.ViewHolder
    {
        TextView instructionItemStepNumber;
        TextView instructionItemStepText;
        public InstructionHolder(@NonNull View itemView) {
            super(itemView);
            instructionItemStepNumber = itemView.findViewById(R.id.instruction_item_step_number);
            instructionItemStepText = itemView.findViewById(R.id.instruction_item_step_text);
        }

    }
}
