package com.example.rs485toandroid.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.rs485toandroid.R;

/**
 * Фрагмент для отображения параметров устройства в виде dashboard
 */
public class DashboardFragment extends Fragment {

    private TextView deviceNameView;
    private TextView temperatureValue;
    private TextView sensorTypeValue;
    private TextView statusValue;
    private TextView slLValue, slHValue, cmpValue, hysValue, donValue, dofValue;
    private TextView tonValue, tofValue, oerValue, dacValue, anLValue, anHValue, ctlValue;
    private TextView protValue, bpsValue, aLenValue, addrValue, rSdLValue;
    private TextView lenValue, prtYValue, sbitValue, nErrValue;
    private TextView prtlValue, aplyValue, initValue;

    public DashboardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initializeViews(view);
        return view;
    }

    /**
     * Инициализирует view элементы фрагмента
     * @param view Корневой view фрагмента
     */
    private void initializeViews(View view) {
        deviceNameView = view.findViewById(R.id.deviceNameView);
        temperatureValue = view.findViewById(R.id.temperatureValue);
        sensorTypeValue = view.findViewById(R.id.sensorTypeValue);
        statusValue = view.findViewById(R.id.statusValue);

        slLValue = view.findViewById(R.id.slLValue);
        slHValue = view.findViewById(R.id.slHValue);
        cmpValue = view.findViewById(R.id.cmpValue);
        hysValue = view.findViewById(R.id.hysValue);
        donValue = view.findViewById(R.id.donValue);
        dofValue = view.findViewById(R.id.dofValue);
        tonValue = view.findViewById(R.id.tonValue);
        tofValue = view.findViewById(R.id.tofValue);
        oerValue = view.findViewById(R.id.oerValue);
        dacValue = view.findViewById(R.id.dacValue);
        anLValue = view.findViewById(R.id.anLValue);
        anHValue = view.findViewById(R.id.anHValue);
        ctlValue = view.findViewById(R.id.ctlValue);

        protValue = view.findViewById(R.id.protValue);
        bpsValue = view.findViewById(R.id.bpsValue);
        aLenValue = view.findViewById(R.id.aLenValue);
        addrValue = view.findViewById(R.id.addrValue);
        rSdLValue = view.findViewById(R.id.rSdLValue);
        lenValue = view.findViewById(R.id.lenValue);
        prtYValue = view.findViewById(R.id.prtYValue);
        sbitValue = view.findViewById(R.id.sbitValue);
        nErrValue = view.findViewById(R.id.nErrValue);

        prtlValue = view.findViewById(R.id.prtlValue);
        aplyValue = view.findViewById(R.id.aplyValue);
        initValue = view.findViewById(R.id.initValue);
    }

    /**
     * Устанавливает название устройства
     * @param name Название устройства
     */
    public void setDeviceName(String name) {
        if (deviceNameView != null) {
            deviceNameView.setText(name);
        }
    }

    /**
     * Сбрасывает все значения на dashboard к значениям по умолчанию
     */
    public void resetDisplay() {
        if (temperatureValue != null) temperatureValue.setText("--.-°C");
        if (sensorTypeValue != null) sensorTypeValue.setText("--");
        if (statusValue != null) statusValue.setText("--");
        if (slLValue != null) slLValue.setText("--");
        if (slHValue != null) slHValue.setText("--");
        if (cmpValue != null) cmpValue.setText("--");
        if (hysValue != null) hysValue.setText("--");
        if (donValue != null) donValue.setText("--");
        if (dofValue != null) dofValue.setText("--");
        if (tonValue != null) tonValue.setText("--");
        if (tofValue != null) tofValue.setText("--");
        if (oerValue != null) oerValue.setText("--");
        if (dacValue != null) dacValue.setText("--");
        if (anLValue != null) anLValue.setText("--");
        if (anHValue != null) anHValue.setText("--");
        if (ctlValue != null) ctlValue.setText("--");
        if (protValue != null) protValue.setText("--");
        if (bpsValue != null) bpsValue.setText("--");
        if (aLenValue != null) aLenValue.setText("--");
        if (addrValue != null) addrValue.setText("--");
        if (rSdLValue != null) rSdLValue.setText("--");
        if (lenValue != null) lenValue.setText("--");
        if (prtYValue != null) prtYValue.setText("--");
        if (sbitValue != null) sbitValue.setText("--");
        if (nErrValue != null) nErrValue.setText("--");
        if (prtlValue != null) prtlValue.setText("--");
        if (aplyValue != null) aplyValue.setText("--");
        if (initValue != null) initValue.setText("--");
    }

    /**
     * Обновляет значение температуры
     * @param value Значение температуры
     */
    public void updateTemperature(float value) {
        if (temperatureValue != null) {
            temperatureValue.setText(String.format("%.1f°C", value));
        }
    }

    /**
     * Обновляет тип датчика
     * @param type Числовое значение типа датчика
     */
    public void updateSensorType(int type) {
        if (sensorTypeValue != null) {
            String[] types = {
                    "tC-K", "tC-J", "Pt100", "Pt100.1", "tC-S", "tC-R",
                    "tC-T", "tC-E", "tC-N", "tC-B", "tC-L", "tC-U",
                    "0-20mA", "0-5V", "0-10V", "4-20mA", "Pt50", "50П",
                    "100П", "46П", "Cu100", "Cu50", "53М", "50М", "100М",
                    "-50..50мВ", "0-1В"
            };
            String typeStr = (type >= 0 && type < types.length) ?
                    types[type] : "Unknown";
            sensorTypeValue.setText(typeStr);
        }
    }

    /**
     * Обновляет статус устройства
     * @param status Значение статуса
     */
    public void updateStatus(int status) {
        if (statusValue != null) {
            StringBuilder statusText = new StringBuilder();
            if ((status & 0x01) != 0) statusText.append("SensorErr ");
            if ((status & 0x08) != 0) statusText.append("CritErr ");
            if ((status & 0x10) != 0) statusText.append("RelayOn");
            if (statusText.length() == 0) statusText.append("OK");
            statusValue.setText(statusText.toString());
        }
    }

    /**
     * Обновляет параметры нагрева
     * @param params Массив параметров нагрева
     */
    public void updateHeatingParams(int[] params) {
        if (params == null || params.length < 13) return;

        if (slLValue != null) slLValue.setText(String.format("%.1f°C", params[0] * 0.1f));
        if (slHValue != null) slHValue.setText(String.format("%.1f°C", params[1] * 0.1f));

        if (cmpValue != null) {
            switch(params[2]) {
                case 0: cmpValue.setText("выкл"); break;
                case 1: cmpValue.setText("нагреватель"); break;
                case 2: cmpValue.setText("холодильник"); break;
                case 3: cmpValue.setText("П-образная"); break;
                case 4: cmpValue.setText("U-образная"); break;
                default: cmpValue.setText(String.valueOf(params[2]));
            }
        }

        if (hysValue != null) hysValue.setText(String.format("%.1f°C", params[3] * 0.1f));
        if (donValue != null) donValue.setText(params[4] + " сек");
        if (dofValue != null) dofValue.setText(params[5] + " сек");
        if (tonValue != null) tonValue.setText(params[6] + " сек");
        if (tofValue != null) tofValue.setText(params[7] + " сек");
        if (oerValue != null) oerValue.setText(params[8] != 0 ? "вкл" : "выкл");
        if (dacValue != null) dacValue.setText(params[9] != 0 ? "П-регулятор" : "регистратор");
        if (anLValue != null) anLValue.setText(String.format("%.1f°C", params[10] * 0.1f));
        if (anHValue != null) anHValue.setText(String.format("%.1f°C", params[11] * 0.1f));
        if (ctlValue != null) ctlValue.setText(params[12] != 0 ? "холодильник" : "нагреватель");
    }

    /**
     * Обновляет параметры связи
     * @param params Массив параметров связи
     */
    public void updateCommParams(int[] params) {
        if (params == null || params.length < 12) return;

        if (protValue != null) {
            String[] protOptions = {"OWEN", "ModBus-RTU", "ModBus-ASCII"};
            protValue.setText(params[0] >= 0 && params[0] < protOptions.length ?
                    protOptions[params[0]] : "Unknown");
        }

        if (bpsValue != null) {
            String[] bpsOptions = {"2.4", "4.8", "9.6", "14.4", "19.2", "28.8", "38.4", "57.6", "115.2"};
            bpsValue.setText(params[1] >= 0 && params[1] < bpsOptions.length ?
                    bpsOptions[params[1]] + " kbps" : "Unknown");
        }

        if (aLenValue != null) {
            aLenValue.setText(params[2] == 0 ? "8 бит" : params[2] == 1 ? "11 бит" : "Unknown");
        }

        if (addrValue != null) {
            addrValue.setText(String.valueOf(params[3]));
        }

        if (rSdLValue != null) {
            rSdLValue.setText(params[4] + " мс");
        }

        if (lenValue != null) {
            lenValue.setText(params[5] == 0 ? "7 бит" : params[5] == 1 ? "8 бит" : "Unknown");
        }

        if (prtYValue != null) {
            prtYValue.setText(params[6] == 0 ? "none" : "Unknown");
        }

        if (sbitValue != null) {
            sbitValue.setText(params[7] == 0 ? "1" : "2");
        }

        if (nErrValue != null) {
            nErrValue.setText(String.format("0x%04X", params[8]));
        }

        if (prtlValue != null) {
            prtlValue.setText(params[9] == 0 ? "неактив" : "актив");
        }

        if (aplyValue != null) {
            aplyValue.setText(params[10] == 0 ? "неактив" : "актив");
        }

        if (initValue != null) {
            initValue.setText(params[11] == 0 ? "неактив" : "актив");
        }
    }
}