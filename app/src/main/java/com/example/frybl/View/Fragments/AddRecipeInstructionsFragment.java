package com.example.frybl.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frybl.Model.Instruction;
import com.example.frybl.R;
import com.example.frybl.View.Activities.AddRecipeActivity;
import com.example.frybl.View.Adapters.InstructionsAdapter;
import com.example.frybl.ViewModel.AddRecipeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AddRecipeInstructionsFragment extends Fragment {

    private RecyclerView instructionsRV;
    private FloatingActionButton floatingActionButton;
    private AddRecipeViewModel viewModel;
    private InstructionsAdapter adapter;
    private Button button;
    private OnClickListenerInterface parentActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_recipe_instructions,container,false);

        instructionsRV = v.findViewById(R.id.add_instructions_recyclerView);
        instructionsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        instructionsRV.setHasFixedSize(true);
        adapter = new InstructionsAdapter();
        instructionsRV.setAdapter(adapter);

        floatingActionButton = v.findViewById(R.id.add_instructions_floatingButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        button = v.findViewById(R.id.add_instructions_save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddRecipeActivity)getActivity()).onAddRecipeClicked();
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.removeRecipeInstruction(adapter.getInstructionAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(instructionsRV);

        return v;
    }


    private void openDialog()
    {
        DialogAddInstructionFragment dialog = new DialogAddInstructionFragment();
        dialog.show(getParentFragmentManager(),"Add instruction");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(AddRecipeViewModel.class);
        viewModel.getRecipeInstructions().observe(getViewLifecycleOwner(), new Observer<List<Instruction>>() {
            @Override
            public void onChanged(List<Instruction> instructions) {
                adapter.setInstructionList(instructions);
            }
        });

    }


    public interface OnClickListenerInterface
    {
        void onAddRecipeClicked();
    }


}
