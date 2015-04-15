package info.bosung.multilevelexpandablelistview;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Eric on 2015-04-14.
 */
public class Content {

    private LinkedHashMap<String, LinkedHashMap<String, List<String>>> data = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, List<String>>> searchedData = new LinkedHashMap<>();
    private boolean isSearch = false;

    public Content()
    {
        LinkedHashMap<String, List<String>> canada = new LinkedHashMap<>();
        List<String> bc = new ArrayList<>();
        bc.add("Whistler");
        bc.add("White Rock");
        canada.put("British Columbia", bc);
        data.put("Canada", canada);

        LinkedHashMap<String, List<String>> us = new LinkedHashMap<>();
        List<String> california = new ArrayList<>();
        california.add("Los Angeles");
        california.add("San Diego");
        us.put("California", california);
        data.put("United States", us);
    }

    private LinkedHashMap<String, LinkedHashMap<String, List<String>>> getData()
    {
        if(isSearch)
        {
            return searchedData;
        }
        else
        {
            return data;
        }
    }

    public void search(String input)
    {
        isSearch = true;
        searchedData.clear();

        for(String country : data.keySet())
        {
            if(country.toLowerCase().contains(input.toLowerCase()))
            {
                //Add this country
                addCountry(country);
            }
            for(String province : data.get(country).keySet())
            {
                if(province.toLowerCase().contains(input.toLowerCase()))
                {
                    //Add this province
                    addProvince(country, province);
                }
                for(String city : data.get(country).get(province))
                {
                    if(city.toLowerCase().contains(input.toLowerCase()))
                    {
                        //Add this city
                        addCity(country, province, city);
                    }
                }
            }
        }
    }

    private void addCountry(String country)
    {
        if(!searchedData.containsKey(country))
        {
            searchedData.put(country, new LinkedHashMap<String, List<String>>());
        }
    }

    private void addProvince(String country, String province)
    {
        addCountry(country);
        if(!searchedData.get(country).containsKey(province))
        {
            searchedData.get(country).put(province, new ArrayList<String>());
        }
    }

    private void addCity(String country, String province, String city)
    {
        addProvince(country, province);
        if(!searchedData.get(country).get(province).contains(city))
        {
            searchedData.get(country).get(province).add(city);
        }
    }

    public void cancelSearch()
    {
        isSearch = false;
    }

    public int getCountrySize()
    {
        return getData().size();
    }

    public String getCountryName(int countryIndex)
    {
        List<String> l = new ArrayList<>(getData().keySet());
        return l.get(countryIndex);
    }

    public LinkedHashMap<String, List<String>> getProvince(int countryIndex)
    {
        List<LinkedHashMap<String, List<String>>> l = new ArrayList<>(getData().values());
        return l.get(countryIndex);
    }

    public int getProvinceSize(int countryIndex)
    {
        return getProvince(countryIndex).size();
    }

    public String getProvinceName(int countryIndex, int provinceIndex)
    {
        LinkedHashMap<String, List<String>> p = getProvince(countryIndex);
        List<String> l = new ArrayList<>(p.keySet());
        return l.get(provinceIndex);
    }

    public List<String> getCity(int countryIndex, int provinceIndex)
    {
        LinkedHashMap<String, List<String>> p = getProvince(countryIndex);
        List<List<String>> l = new ArrayList<>(p.values());
        return l.get(provinceIndex);
    }

    public int getCitySize(int countryIndex, int provinceIndex)
    {
        return getCity(countryIndex, provinceIndex).size();
    }

    public String getCityName(int countryIndex, int provinceIndex, int cityIndex)
    {
        List<String> c = getCity(countryIndex, provinceIndex);
        return c.get(cityIndex);
    }
}
