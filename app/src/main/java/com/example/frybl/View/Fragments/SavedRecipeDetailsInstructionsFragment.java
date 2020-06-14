package com.example.frybl.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frybl.Model.Instruction;
import com.example.frybl.R;
import com.example.frybl.View.Adapters.InstructionsAdapter;
import com.example.frybl.ViewModel.SavedRecipeDetailsViewModel;

import java.util.List;

public class SavedRecipeDetailsInstructionsFragment extends Fragment {

    private RecyclerView recyclerView;
    private InstructionsAdapter instructionsAdapter;
    private SavedRecipeDetailsViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(SavedRecipeDetailsViewModel.class);
        viewModel.getInstructionLiveData().observe(getActivity(), new Observer<List<Instruction>>() {
            @Override
            public void onChanged(List<Instruction> instructions) {
                instructionsAdapter.setInstructionList(instructions);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upload_details_instructions,container,false);

        instructionsAdapter = new InstructionsAdapter();
        recyclerView = v.findViewById(R.id.upload_details_instructions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(instructionsAdapter);

        return v;
    }



}
