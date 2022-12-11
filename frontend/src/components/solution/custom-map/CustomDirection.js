import React, { Component } from "react";
const { DirectionsRenderer } = require("react-google-maps");

class CustomDirection extends Component {
  state = {
    wayPoints: null,
    currentLocation: null
  };
  delayFactor = 0;

  componentDidMount() {
    const startLoc = this.props.from.latitude + ", " + this.props.from.longitude;
    const destinationLoc = this.props.to.latitude + ", " + this.props.to.longitude;
    this.getDirections(startLoc, destinationLoc);
    this.setCurrentLocation();
  }

  async getDirections(startLoc, destinationLoc, wayPoints = []) {
    const waypts = [];
    if (wayPoints.length > 0) {
      waypts.push({
        location: new window.google.maps.LatLng(
          wayPoints[0].lat,
          wayPoints[0].lng
        ),
        stopover: true
      });
    }
    const directionService = new window.google.maps.DirectionsService();
    directionService.route(
      {
        origin: startLoc,
        destination: destinationLoc,
        waypoints: waypts,
        optimizeWaypoints: true,
        travelMode: window.google.maps.TravelMode.DRIVING
      },
      (result, status) => {
        // console.log("status", status);
        if (status === window.google.maps.DirectionsStatus.OK) {

          this.setState({
            directions: result,
            wayPoints: result.routes[0].overview_path.filter((elem, index) => {
              return index % 30 === 0;
            })
          });
        } else if (
          status === window.google.maps.DirectionsStatus.OVER_QUERY_LIMIT
        ) {
          this.delayFactor += 1;
          setTimeout(() => {
            this.getDirections(startLoc, destinationLoc, wayPoints);
          }, this.delayFactor * 1000);
        } else {
          console.error(`error fetching directions ${result}`);
        }
      }
    );
  }

  setCurrentLocation = () => {
    let count = 0;
  };
  render() {
    const directions = this.state.directions;
    console.log(this.props.from);
    console.log(this.props.to);
    console.log(directions);
    return (
      <div>
        {this.state.directions && (
          <DirectionsRenderer
            key={this.props.key}
            directions={this.state.directions}
            options={{
              polylineOptions: {
                strokeColor: this.props.strokeColor,
                strokeOpacity: 1,
                strokeWeight: 3,
                icons: [{ repeat: '400px', icon: { path: window.google.maps.SymbolPath.FORWARD_OPEN_ARROW, scale: 3 } }]

              },
              preserveViewport: true,
              suppressMarkers: true,
              icon: {
                scale: 1,

              }
            }}
          />
        )}
      </div>
    );
  }
}

export default CustomDirection;
