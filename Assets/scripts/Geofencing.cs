#if UNITY_ANDROID
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;
using UnityEngine.Android;

public class Geofencing : MonoBehaviour
{
    AndroidJavaClass unityPlayerClass;
    public AndroidJavaObject unityActivity;
    AndroidJavaObject bridge;
    public GeofencingInput[] geofencingInput;

    object[] parameters;

    public string notificationContentTitle = "Title";
    public string notificationContentText = "ContentText";
    public string notificationDescription = "Description";
    public string notificationUserVisibleName = "IbleEthan";

    [SerializeField] TMP_InputField iD;
    [SerializeField] TMP_InputField latitude;
    [SerializeField] TMP_InputField longitude;
    [SerializeField] TMP_InputField radius;
    [SerializeField] TMP_InputField loiteringDelay;


    void Start()
    {
        CallNativePlugin();
    }
    public void CallNativePlugin()
    {
        unityPlayerClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        unityActivity = unityPlayerClass.GetStatic<AndroidJavaObject>("currentActivity");

        parameters = new object[5];
        parameters[0] = unityActivity;
        parameters[1] = notificationContentTitle;
        parameters[2] = notificationContentText;
        parameters[3] = notificationDescription;
        parameters[4] = notificationUserVisibleName;

        bridge = new AndroidJavaObject("com.ethan.mygeofencing.Bridge", parameters);

        // Call addGeoFence in bridge, with our parameters.
        if (geofencingInput.Length > 0)
        {
            foreach (var item in geofencingInput)
            {
                bridge.Call("addGeoFence", item.id, item.latitude, item.longitude, item.radius, item.loiteringDelay);
            }
            bridge.Call("GeoFenceCompleted");
        }
    }
    public void AddGeoFence()
    {
        double latitude = double.Parse(this.latitude.text);
        double longitude = double.Parse(this.longitude.text);
        float radius = float.Parse(this.radius.text);
        int loiteringDelay = int.Parse(this.loiteringDelay.text);
        //int thansitionTypes = 1;
        bridge.Call("addGeoFence", iD.text, latitude, longitude, radius, loiteringDelay);
        bridge.Call("GeoFenceCompleted");
    }

    public void RemoveGeoFence(string[] ids)
    {
        bridge.Call("removeGeoFences", ids);
    }

    public void RemoveAllGeoFence()
    {
        bridge.Call("removeAllGeoFences");
    }

    [System.Serializable]
    public class GeofencingInput
    {
        [Tooltip("名稱")]
        public string id;
        [Tooltip("經度")]
        public double latitude;
        [Tooltip("緯度")]
        public double longitude;
        [Tooltip("半徑100-300")]
        public float radius;
        [Tooltip("ms")]
        public int loiteringDelay;
        //[Tooltip("觸發類型")]
        //public ThansitionTypes thansitionTypes;
    }
    //public enum ThansitionTypes
    //{
    //    ENTER = 1,
    //    EXIT = 2,
    //    DWELL = 4
    //}
}
#endif