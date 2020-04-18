package com.example.covid;

import org.json.JSONException;
import org.json.JSONObject;

class SortBasedOncases implements java.util.Comparator<JSONObject> {
    @Override
    public int compare(JSONObject lhs, JSONObject rhs) {

            String valA = new String();
            String valB = new String();
        long A=0,B=0;

            try {
                valA = (String) lhs.getString("cases");
                valB = (String) rhs.getString("cases");
                 A= Long.parseLong(valA);
                 B= Long.parseLong(valB);

                System.out.println(A+", "+B);

            }
            catch (JSONException e) {
                //do something
            }
            if(A>B) {
                return 1;
            }

                else
                    return 0;

            //return valA.compareTo(valB);


    }
}
