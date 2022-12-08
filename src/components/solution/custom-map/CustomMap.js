import React from "react";
// react components used to create a google map
import {
  withScriptjs,
  withGoogleMap,
  GoogleMap,
  Marker
} from "react-google-maps";

const HN_COOR = { lat: 21.028511, lng: 105.804817 };

const CustomMap = withScriptjs(
  withGoogleMap(props => (
    <GoogleMap
      defaultZoom={13}
      defaultCenter={HN_COOR}
      defaultOptions={{
        scrollwheel: false,
        zoomControl: true
      }}
    >
      {/* <Marker position={{ lat: 40.748817, lng: -73.985428 }} /> */}
    </GoogleMap>
  ))
);

function Maps({ ...prop }) {
  return (
    <CustomMap
      googleMapURL="https://maps.googleapis.com/maps/api/js?key=AIzaSyAmoJa0osaR-pbmamp2GTxdAzE-rBKF1hs"
      loadingElement={<div style={{ height: `100%` }} />}
      containerElement={<div style={{ height: `100vh` }} />}
      mapElement={<div style={{ height: `100%` }} />}
    />
  );
}

export default Maps;
