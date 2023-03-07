using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;


public class HelloFromAndroid : MonoBehaviour
{
    public TMP_Text status;
    // Start is called before the first frame update
    void Start()
    {

    }

    // Update is called once per frame
    void Update()
    {

    }

    public void GetMessageFromAndroid(string eventStatus)
    {
        switch (eventStatus)
        {
            case "onStart":
                {
                    Debug.Log("onStart Received");
                    status.text = "app is opened from notification";
                }
                break;
            default:
                break;
        }
    }
    public void OnSetGeofenceButtonClick()
    {
        status.text = "SetGeofence";
    }

    public void RemoveAllGeofenceButtonClick()
    {
        status.text = "RemoveAllGeofence";
    }
}
