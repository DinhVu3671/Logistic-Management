import React, { useState, useEffect, Component } from 'react'
import { useHistory, useLocation } from 'react-router-dom'

import {
    CRow,
    CContainer,
    CButtonGroup,
    CButton,
} from '@coreui/react'
import queryString from 'query-string';
import randomColor from 'randomcolor';
import GoogleMap from 'react-google-maps/lib/components/GoogleMap';
import CustomDirection from '../custom-map/CustomDirection';
import MapControl from '../custom-map/MapControl';
import TimelineJourney from '../TimelineJourney';
import { compose, withProps } from "recompose";
import withScriptjs from 'react-google-maps/lib/withScriptjs';
import withGoogleMap from 'react-google-maps/lib/withGoogleMap';
import routeService from '../../../service/SolutionRouteService';
import journeyDriverService from '../../../service/JourneyDriverService';
import { checkExistNode, getCustomersOfJourney, getDepotsOfJourney } from "../../../utilities/utility";

import VehicleMarker from '../custom-map/VehicleMarker';
import CustomerMarker from '../custom-map/CustomerMarker';
import DepotMarker from '../custom-map/DepotMarker';


const HN_COOR = { lat: 21.028511, lng: 105.804817 };

class TrackingRoute extends Component {
    constructor(props) {
        super(props);
        const { location } = props;
        let params = queryString.parse(location.search);
        const { id } = params;
        const idSolutionRouting = id;
        this.state = ({
            idSolutionRouting: idSolutionRouting,
            defaultZoom: 13,
            center: HN_COOR,
            isProgress: false,
            showTimeline: false,
            showInputInfo: false,
            problemAssumption: this.props.problemAssumption,
            isTracking: false,
        });
    }

    componentDidMount() {
        let search = {
            id: this.state.idSolutionRouting
        }
        routeService.trackingRoute(search).then(response => {
            const trackingRoute = response.data.data;
            trackingRoute.solution.id = trackingRoute.id;
            trackingRoute.solution.isSelected = trackingRoute.isSelected;
            let solutions = [trackingRoute.solution];
            this.processSolutions(solutions);
            this.setState({
                trackingRoute: trackingRoute,
                solution: solutions[0],
                solutions: solutions,
                directions: solutions[0].directions,
                customers: this.setCustomersMarker(trackingRoute.problemAssumption.customers),
                depots: this.setDepotsMarker(trackingRoute.problemAssumption.depots),
                problemAssumption: trackingRoute.problemAssumption,
                showDepotMarker: true,
            });

        }).catch(e => {
            console.log(e);
        });
    }

    setDepotsMarker = (depotsData) => {
        depotsData.map((depot) => {
            depot.marker = {
                id: depot.id,
                code: depot.code,
                lat: depot.latitude,
                lng: depot.longitude,
                color: 'white',
                isShowed: true,
            }
        });
        return depotsData;
    }

    setCustomersMarker = (customersData) => {
        customersData.map((customer) => {
            customer.marker = {
                id: customer.id,
                code: customer.code,
                lat: customer.latitude,
                lng: customer.longitude,
                isShowed: true,
            }
        });
        return customersData;

    }

    setShowMarkerInfo = (marker) => {
        let customers = this.state.customers.map((customer) => {
            if (customer.code === marker.code) {
                let showMarker = customer.marker;
                showMarker.isShowInfo = marker.isShowInfo;
                showMarker.infoWindow = marker.infoWindow;
                customer.marker = showMarker;
                return customer;
            } else {
                return customer;
            }
        });
        let depots = this.state.depots.map((depot) => {
            if (depot.code === marker.code) {
                let showMarker = depot.marker;
                showMarker.isShowInfo = marker.isShowInfo;
                showMarker.infoWindow = marker.infoWindow;
                depot.marker = showMarker;
                return depot;
            } else {
                return depot;
            }
        });
        this.setState({
            customers: customers,
            depots: depots,
        });
    }

    showCustomerMarkerInfo = (marker) => {
        let customers = this.state.customers.map((customer) => {
            if (customer.code === marker.code) {
                let showMarker = customer.marker;
                showMarker.isShowInfo = true;
                showMarker.infoWindow = marker.infoWindow;
                customer.marker = showMarker;
                return customer;
            } else {
                return customer;
            }
        });
        this.setState({ customers: customers });
    }

    showDepotMarkerInfo = (marker) => {
        let depots = this.state.depots.map((depot) => {
            if (depot.code === marker.code) {
                let showMarker = depot.marker;
                showMarker.isShowInfo = true;
                showMarker.infoWindow = marker.infoWindow;
                depot.marker = showMarker;
                return depot;
            } else {
                return depot;
            }
        });
        this.setState({ depots: depots });
    }

    showDepotMarkerInfo = (marker) => {
        let depots = this.state.depots.map((depot) => {
            if (depot.code === marker.code) {
                let showMarker = depot.marker;
                showMarker.isShowInfo = true;
                showMarker.infoWindow = marker.infoWindow;
                depot.marker = showMarker;
                return depot;
            } else {
                return depot;
            }
        });
        this.setState({ depots: depots });
    }

    setDirections = (solution) => {
        let directions = [];
        solution.journeys.map((journey, journeyIndex) => {
            let directionColor = randomColor();
            let directionOfJourney = [];
            journey.routes.map((route, routeIndex) => {
                let directionArcs = route.arcs.map((arc, arcIndex) => {
                    return ({
                        id: 1000 * journeyIndex + 100 * routeIndex + arcIndex,
                        from: arc.fromNode,
                        to: arc.toNode,
                        strokeColor: directionColor,
                        isShowed: true,
                    });
                });
                directionOfJourney = directionOfJourney.concat(directionArcs);
            });
            directions = directions.concat(directionOfJourney);
            journey.directions = directionOfJourney;
        });
        solution.directions = directions;
    }
    

    setShowTimeline = (showTimeline) => {
        this.setState({ showTimeline: showTimeline })
    }

    setShowInputInfo = (showInputInfo) => {
        this.setState({ showInputInfo: showInputInfo })
    }

    processSolutions = (solutions) => {
        solutions = solutions.map((solution, index) => {
            solution.efficiency = Math.round(solution.efficiency * 100) / 100;
            solution.focusJourneyId = 0;
            this.setDirections(solution);
            return solution;
        })
    }

    changeSolution = (solution) => {
        this.setState({ directions: [] });
        this.setState({
            solution: solution,
            // directions: solution.directions,
        });
        setTimeout(() => {
            this.setState({
                directions: solution.directions,
            });
        }, 2000);
        setTimeout(() => {
            this.showAllJourney();
        }, 2500);

    }

    focusCustomers = (showedCustomers) => {
        let customers = this.state.customers;
        this.setState({ customers: [] });
        customers.map((customer, index) => {
            let idx = checkExistNode(customer, showedCustomers);
            if (idx !== null) {
                customer.marker.isShowed = true;
                customer.marker.code = (idx + 1) + "";
            } else {
                customer.marker.isShowed = false;
            }
        });
        setTimeout(() => {
            this.setState({ customers: customers });
        }, 200
        );
    }

    focusDepots = (showedDepots) => {
        let depots = this.state.depots;
        depots.map((depot, index) => {
            depot.marker.isShowed = checkExistNode(depot, showedDepots) === null ? false : true;
        });
        this.setState({ depots: depots });
    }

    focusDirections = (showedDirections) => {
        let directions = this.state.directions;
        directions.map((direction, index) => {
            direction.isShowed = showedDirections.find(showedDirection => showedDirection.id === direction.id) === undefined ? false : true;
        });
        this.setState({ directions: directions });
    }

    focusJourney = (journey) => {
        let solution = this.state.solution;
        solution.focusJourneyId = journey.id;
        this.setState({ solution });
        let customersOfJourney = getCustomersOfJourney(journey);
        let depotsOfJourney = getDepotsOfJourney(journey);
        this.focusCustomers(customersOfJourney);
        this.focusDepots(depotsOfJourney);
        this.focusDirections(journey.directions);
    }

    showAllJourney = () => {
        let depots = this.state.depots;
        depots.map((depot) => {
            depot.marker.isShowed = true;
            depot.marker.code = depot.code;
        });
        let customers = this.state.customers;
        this.setState({ customers: [] });
        customers.map((customer) => {
            customer.marker.isShowed = true;
            customer.marker.code = customer.code;
        });
        let directions = this.state.directions;
        directions.map((direction) => {
            direction.isShowed = true;
        });
        let solution = this.state.solution;
        solution.focusJourneyId = 0;
        setTimeout(() => {
            this.setState({
                depots: depots,
                customers: customers,
                solution: solution,
                directions: directions
            });
        }, 250
        )
    }

    trackingJourney = () => {
        let trackingSearch = {
            solutionRoutingId: this.state.idSolutionRouting,
            intendReceiveTime: this.state.solution.intendReceiveTime
        }
        journeyDriverService.tracking(trackingSearch).then(response => {
            if (response.data.code === "SUCCESS") {
                let journeyDrivers = response.data.data;
                console.log("journeyDrivers", journeyDrivers);
                this.setJourneyDrivers(journeyDrivers);
            }
        }).catch(e => {
            console.log(e);
        });
    }

    startTracking = () => {
        this.trackingJourney();
        const tracking = setInterval(() => {
            console.log("start Tracking interval");
            this.trackingJourney();
        }, 60 * 1000);
        this.setState({ tracking: tracking });

    }

    stopTracking = () => {
        clearInterval(this.state.tracking)
    }

    handleTracking = (isTracking) => {
        this.setState({
            isTracking: isTracking
        })
        if (isTracking)
            this.startTracking();
        else
            this.stopTracking();
    }

    checkDuplicateMarker = (journeyDriver, index, journeyDrivers) => {
        for (let eleIndex = index + 1; eleIndex < journeyDrivers.length; eleIndex++) {
            const element = journeyDrivers[eleIndex];
            if (journeyDriver.actualJourney.currentLat === element.actualJourney.currentLat
                && journeyDriver.actualJourney.currentLng === element.actualJourney.currentLng) {
                journeyDriver.actualJourney.currentLat += 0.0001;
                journeyDriver.actualJourney.currentLat += 0.0001;
            }
        }
    }

    setJourneyDrivers = (journeyDrivers) => {
        journeyDrivers.map((journeyDriver, index) => {
            this.checkDuplicateMarker(journeyDriver, index, journeyDrivers);
            journeyDriver.marker = {
                id: index + 1,
                lat: journeyDriver.actualJourney.currentLat,
                lng: journeyDriver.actualJourney.currentLng,
                infoWindow: {
                    vehicle: journeyDriver.vehicle,
                    driver: journeyDriver.driver,
                    actualJourney: journeyDriver.actualJourney,
                    mustDeliveryOrders: journeyDriver.mustDeliveryOrders,
                },
                type: journeyDriver.vehicle.type
            }
            console.log("marker", journeyDriver.marker);
        })
        this.setState({
            journeyDrivers: journeyDrivers
        });
    }

    render() {
        return (
            <CContainer>
                <CRow>
                    <GoogleMap
                        defaultZoom={this.state.defaultZoom}
                        center={this.state.center}
                        defaultCenter={new window.google.maps.LatLng(HN_COOR.lat, HN_COOR.lng)}
                    >
                        {this.state.directions && this.state.directions.map((direction, index) => {
                            if (direction.isShowed === true)
                                return (
                                    <CustomDirection
                                        key={index + 1}
                                        index={index + 1}
                                        strokeColor={direction.strokeColor}
                                        from={direction.from}
                                        to={direction.to}
                                    />
                                );
                        })}
                        {this.state.customers && this.state.customers.map((customer, index) => {
                            if (customer.marker.isShowed === true)
                                return (
                                    <CustomerMarker
                                        key={index + 1}
                                        marker={customer.marker}
                                        setShow={this.setShowMarkerInfo}
                                    >
                                    </CustomerMarker>
                                );
                        })}
                        {this.state.depots && this.state.depots.map((depot, index) => {
                            if (depot.marker.isShowed === true)
                                return (
                                    <DepotMarker
                                        key={index + 1}
                                        marker={depot.marker}
                                        setShow={this.setShowMarkerInfo}
                                        type={"DEPOT"}
                                    >
                                    </DepotMarker>
                                );
                        })}


                        {this.state.journeyDrivers && this.state.journeyDrivers.map((journeyDriver, index) => {
                            return (
                                <VehicleMarker
                                    key={index + 1}
                                    marker={journeyDriver.marker}
                                >
                                </VehicleMarker>
                            );
                        })}


                        {this.state.showTimeline === true &&
                            <MapControl position={window.google.maps.ControlPosition.BOTTOM_CENTER}>
                                <TimelineJourney solutions={this.state.solutions}
                                    solution={this.state.solution}
                                    changeSolution={this.changeSolution}
                                    setDirections={this.setDirections}
                                    showCustomerMarkerInfo={this.showCustomerMarkerInfo}
                                    showDepotMarkerInfo={this.showDepotMarkerInfo}
                                    problemAssumption={this.state.problemAssumption} 
                                    focusJourney={this.focusJourney}
                                    showAllJourney={this.showAllJourney}
                                    />
                            </MapControl>
                        }
                        {this.state.solutions &&
                            <MapControl position={window.google.maps.ControlPosition.RIGHT_TOP}>
                                <CButtonGroup>
                                    <CButton type="submit" size="md" color="success" onClick={() => this.setShowTimeline(!this.state.showTimeline)}>{this.state.showTimeline === true ? "Hide Timeline" : "Show Timeline"}</CButton>
                                </CButtonGroup>
                            </MapControl>
                        }
                        {this.state.solution && this.state.solution.isSelected === true &&
                            <MapControl position={window.google.maps.ControlPosition.RIGHT_TOP}>
                                <CButtonGroup>
                                    <CButton type="submit" size="md" color="primary" onClick={() => this.handleTracking(!this.state.isTracking)}>{this.state.isTracking === true ? "Stop Tracking" : "Tracking"}</CButton>
                                </CButtonGroup>
                            </MapControl>
                        }
                    </GoogleMap>
                </CRow>

            </CContainer>
        )
    }


}

export default compose(
    withProps({
        googleMapURL: "https://maps.googleapis.com/maps/api/js?key=AIzaSyAmoJa0osaR-pbmamp2GTxdAzE-rBKF1hs",
        loadingElement: <div style={{ height: `100%` }} />,
        containerElement: <div style={{ height: `100vh` }} />,
        mapElement: <div style={{ height: `100%` }} />
    }),
    withScriptjs,
    withGoogleMap
)(TrackingRoute);