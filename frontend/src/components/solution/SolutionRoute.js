import { CButton, CButtonGroup, CCard, CCardBody, CCardHeader, CCol, CContainer, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CProgress, CRow, CToast, CToastBody, CToaster, CToastHeader } from "@coreui/react";
import React, { Component } from "react";
import { compose, withProps } from "recompose";
import CustomDirection from "./custom-map/CustomDirection";
import randomColor from "randomcolor";
import TimelineJourney from "./TimelineJourney";

import { GoogleMap, withScriptjs, withGoogleMap, } from "react-google-maps"
import MapControl from "./custom-map/MapControl";
import RouteInitInfoTabs from "./RouteInitInfoTabs";
import { checkExistNode, getCustomersOfJourney, getDepotsOfJourney } from "../../utilities/utility";
import CustomerMarker from "./custom-map/CustomerMarker";
import DepotMarker from "./custom-map/DepotMarker";
import { withNamespaces } from 'react-i18next';

const HN_COOR = { lat: 21.028511, lng: 105.804817 };

class SolutionRoute extends Component {

    constructor(props) {
        super(props);
        this.state = {
            defaultZoom: 13,
            center: HN_COOR,
            depots: this.props.depots,
            customers: this.props.customers,
            orders: this.props.orders,
            vehicles: this.props.vehicles,
            solutions: this.props.solutions,
            solution: this.props.solutions[0],
            directions: this.props.solutions[0].directions,
            isProgress: false,
            showTimeline: false,
            showInputInfo: false,
            showReInitializeConfirm: false,
            problemAssumption: this.props.problemAssumption,
        };
    }

    componentDidMount() {
        let problemAssumption = this.props.problemAssumption;
        // problemAssumption.customers = this.props.customers;
        problemAssumption.depots = this.props.depots;
        problemAssumption.orders = this.props.orders;
        problemAssumption.vehicles = this.props.vehicles;
        this.setState({
            problemAssumption: problemAssumption
        });
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
        console.log(marker);
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
        console.log(customers);
        this.setState({ customers: customers });
    }

    showDepotMarkerInfo = (marker) => {
        console.log(marker);
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
        console.log(depots);
        this.setState({ depots: depots });
    }

    processDirections = (solution) => {
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

    resetFocus() {
        let solution = this.state.solution;
        solution.focusJourneyId = 0;
        this.setState({
            solution: solution
        });
        let solutions = this.state.solutions;
        solutions.map(solution => { solution.focusJourneyId = 0 });
        this.setState({
            solutions: solutions,
        });
    }

    setShowTimeline = (showTimeline) => {
        if (showTimeline === true) {
            this.resetFocus();
            this.showAllJourney();
        }
        this.setState({ showTimeline: showTimeline })

    }

    setShowInputInfo = (showInputInfo) => {
        this.setState({ showInputInfo: showInputInfo })
    }

    setShowReInitializeConfirm = (showReInitializeConfirm) => {
        this.setState({ showReInitializeConfirm: showReInitializeConfirm })
    }

    handleReInitialize = () => {
        this.props.reInitialize();
    }

    processSolutions = (solutions) => {
        solutions = solutions.map((solution, index) => {
            solution.id = index + 1;
            solution.efficiency = Math.round(solution.efficiency * 100) / 100;
            this.processDirections(solution);
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

    setShowToast = (showToast) => {
        this.setState({
            showToast: showToast
        });
    }

    render() {
        const { t } = this.props;
        return (
            <CContainer>
                {(this.props.values.isInitFail === false) &&
                    <CToaster position='top-right'>
                        <CToast
                            key={'toastSuccess'}
                            show={true}
                            autohide={4000}
                            fade={true}
                            onStateChange={(showToast) => { this.setShowToast(showToast) }}
                        >
                            <CToastHeader closeButton>{t("solutionRoute.initSuccess.title")}</CToastHeader>
                            <CToastBody>
                                {t("solutionRoute.initSuccess.body")}
                            </CToastBody>
                        </CToast>
                    </CToaster>

                }
                <CRow>
                    {this.state.showReInitializeConfirm === true &&
                        <CModal
                            show={true}
                            onClose={() => this.setShowReInitializeConfirm(false)}
                            color="warning"
                        >
                            <CModalHeader closeButton>
                                <CModalTitle>Confirm Re-initialize Routes</CModalTitle>
                            </CModalHeader>
                            <CModalBody>
                                <h6>Xác nhận khởi tạo lại lộ trình ?</h6>
                                <small><i>Quá trình khởi tạo sẽ trở về ban đầu</i></small>
                            </CModalBody>
                            <CModalFooter>
                                <CButton color="success" onClick={() => this.handleReInitialize()}>Confirm</CButton>{' '}
                                <CButton color="secondary" onClick={() => this.setShowReInitializeConfirm(false)}>Cancel</CButton>
                            </CModalFooter>
                        </CModal>
                    }

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
                        {this.state.showTimeline === true &&
                            <MapControl position={window.google.maps.ControlPosition.BOTTOM_CENTER}>
                                <TimelineJourney solutions={this.state.solutions}
                                    solution={this.state.solution}
                                    setSolution={this.changeSolution}
                                    setDirections={this.processDirections}
                                    showCustomerMarkerInfo={this.showCustomerMarkerInfo}
                                    showDepotMarkerInfo={this.showDepotMarkerInfo}
                                    problemAssumption={this.state.problemAssumption}
                                    focusJourney={this.focusJourney}
                                    showAllJourney={this.showAllJourney} />
                            </MapControl>
                        }
                        {this.state.solutions &&
                            <MapControl position={window.google.maps.ControlPosition.RIGHT_TOP}>
                                <CButtonGroup>
                                    <CButton type="submit" size="md" color="success" onClick={() => this.setShowTimeline(!this.state.showTimeline)}>{this.state.showTimeline === true ? t("solutionRoute.buttonTimeline.hide") : t("solutionRoute.buttonTimeline.show")}</CButton>
                                </CButtonGroup>
                            </MapControl>
                        }
                        {/* {this.state.solutions &&
                            <MapControl position={window.google.maps.ControlPosition.RIGHT_TOP}>
                                <CButtonGroup>
                                    <CButton type="submit" size="md" color="primary" onClick={() => this.setShowInputInfo(!this.state.showInputInfo)}>{this.state.showInputInfo === true ? "Hide Input" : "Show Input"}</CButton>
                                </CButtonGroup>
                            </MapControl>
                        }
                        {this.state.solutions &&
                            <MapControl position={window.google.maps.ControlPosition.RIGHT_TOP}>
                                <CButtonGroup>
                                    <CButton type="submit" size="md" color="warning" onClick={() => this.setShowReInitializeConfirm(true)}>{"Re-initialize"}</CButton>
                                </CButtonGroup>
                            </MapControl>
                        } */}
                        {this.state.showInputInfo === true &&
                            <MapControl position={window.google.maps.ControlPosition.TOP_LEFT}>
                                <RouteInitInfoTabs
                                    depots={this.props.depots}
                                    customers={this.props.customers}
                                    orders={this.props.orders}
                                    vehicles={this.props.vehicles}
                                    problemAssumption={this.props.problemAssumption}
                                ></RouteInitInfoTabs>
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
)(withNamespaces('solution')(SolutionRoute));