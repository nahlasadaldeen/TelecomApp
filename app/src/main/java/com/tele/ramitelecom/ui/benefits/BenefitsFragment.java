package com.tele.ramitelecom.ui.benefits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.card.MaterialCardView;
import com.tele.ramitelecom.R;
import com.tele.ramitelecom.db.TransferTableOperations;
import com.tele.ramitelecom.db.UnitPriceTableOperations;
import com.tele.ramitelecom.ui.users.TransferModel;
import com.tele.ramitelecom.ui.users.UnitPriceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BenefitsFragment extends Fragment {

    Button btnBenefitsForOne, btnBenefitsForAll;

    TextView txtAllCost, txtSells, txtBenefits;
    MaterialCardView pnlBenefits;
    private BenefitsViewModel benefitsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        benefitsViewModel = ViewModelProviders.of(this).get(BenefitsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_benefits, container, false);

        pnlBenefits = root.findViewById(R.id.pnlBenefits);
        btnBenefitsForOne = root.findViewById(R.id.btnBenefitsForOne);

        btnBenefitsForAll = root.findViewById(R.id.btnBenefitsForAll);

        txtAllCost = root.findViewById(R.id.txtAllCost);
        txtSells = root.findViewById(R.id.txtSells);
        txtBenefits = root.findViewById(R.id.txtBenefits);

        btnBenefitsForOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pnlBenefits.setVisibility(View.VISIBLE);
                // get cost
                // get sells
                TransferTableOperations transferTableOperations = new TransferTableOperations(requireContext());
                List<TransferModel> transfersList = transferTableOperations.getOneTransferData();
                int allSells = 0;
                int allCosts = 0;
                for (int i = 0; i < transfersList.size(); i++) {
                    allSells += transfersList.get(i).money_amount;
                    allCosts += calculateBalanceForOne(transfersList.get(i).money_amount);
                }
                // calculate benefits
                int benefits = allSells - allCosts;

                txtAllCost.setText(allCosts + "");
                txtSells.setText(allSells + "");
                txtBenefits.setText(benefits + "");


            }
        });

        btnBenefitsForAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pnlBenefits.setVisibility(View.VISIBLE);

                // get cost
                // get sells
                TransferTableOperations transferTableOperations = new TransferTableOperations(requireContext());
                List<TransferModel> transfersList = transferTableOperations.getAllTransferData();
                int allSells = 0;
                int allCosts = 0;
                for (int i = 0; i < transfersList.size(); i++) {
                    allSells += transfersList.get(i).getMoney_amount();
                    allCosts += calculateBalanceForAll(transfersList.get(i).getMoney_amount());
                }
                // calculate benefits
                int benefits = allSells - allCosts;

                txtAllCost.setText(allCosts + "");
                txtSells.setText(allSells + "");
                txtBenefits.setText(benefits + "");

            }
        });

        return root;

    }

    private int calculateBalanceForOne(int price) {
        int balanceRes = 0;
        UnitPriceTableOperations unitPriceTableOperations = new UnitPriceTableOperations(requireContext());
        ArrayList<UnitPriceModel> res = unitPriceTableOperations.getAllUnitsData();
        HashMap<Integer, Integer> allPrices = new HashMap<>();
        for (int i = 0; i < res.size(); i++) {
            UnitPriceModel unitPriceModel = res.get(i);
            allPrices.put(unitPriceModel.unit_price, unitPriceModel.unit_class);
        }
        Integer priceOfUnits = allPrices.get(price);
        if (priceOfUnits != null)
            balanceRes = priceOfUnits;

       /* switch (price) {
            case 75: {
                balanceRes = 50;
                break;
            }
            case 100: {
                balanceRes = 90;
                break;
            }
            case 125: {
                balanceRes = 100;
                break;
            }
            case 175: {
                balanceRes = 150;
                break;
            }
            case 225: {
                balanceRes = 200;
                break;
            }
            case 250: {
                balanceRes = 225;
                break;
            }
            case 300: {
                balanceRes = 250;
                break;
            }
            case 350: {
                balanceRes = 300;
                break;
            }
            case 500: {
                balanceRes = 450;
                break;
            }
            case 550: {
                balanceRes = 500;
                break;
            }
            case 750: {
                balanceRes = 650;
                break;
            }
            case 850: {
                balanceRes = 750;
                break;
            }
            case 1000: {
                balanceRes = 900;
                break;
            }
            case 1100: {
                balanceRes = 1000;
                break;
            }
            case 1350: {
                balanceRes = 1200;
                break;
            }
            case 1650: {
                balanceRes = 1500;
                break;
            }
            case 2200: {
                balanceRes = 2000;
                break;
            }
            case 4400: {
                balanceRes = 4000;
                break;
            }
            case 5500: {
                balanceRes = 5000;
                break;
            }
            case 11000: {
                balanceRes = 10000;
                break;
            }
            case 15500: {
                balanceRes = 14381;
                break;
            }
            case 16500: {
                balanceRes = 15000;
                break;
            }
            case 22000: {
                balanceRes = 20000;
                break;
            }
        }*/
        return balanceRes;
    }

    private int calculateBalanceForAll(int price) {
        if (price >= 50 && price < 100) {
            double sum = price - 1;
            return (int) sum;
        } else if (price >= 100) {
            double sum = price - (price * 0.01);
            return (int) sum;
        } else if (price < 50 && price > 1) return price;
        else {
            return 0;
        }
    }

}