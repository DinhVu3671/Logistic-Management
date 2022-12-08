import React, { useState, useEffect, Component } from 'react'
import { useHistory, useLocation } from 'react-router-dom'
import Timeline, { DateHeader, SidebarHeader, TimelineHeaders } from 'react-calendar-timeline'
import 'react-calendar-timeline/lib/Timeline.css'
import moment from 'moment'
import { CButton, CCard, CCardBody, CCardHeader, CCol, CContainer, CFormGroup, CLabel, CModal, CModalBody, CModalFooter, CModalHeader, CModalTitle, CRow, CSelect, CSpinner, CToast, CToastBody, CToaster, CToastHeader } from '@coreui/react'
import { getCustomersOfJourney, secondsToHHMMSS } from '../../utilities/utility'
import routeService from '../../service/SolutionRouteService'
import CIcon from '@coreui/icons-react'
import SaveSolutionRouting from './delivery-plan/SaveSolutionRouting'
import { withNamespaces } from 'react-i18next';

var keys = {
    groupIdKey: "id",
    groupTitleKey: "title",
    groupRightTitleKey: "rightTitle",
    itemIdKey: "id",
    itemTitleKey: "title",
    itemDivTitleKey: "title",
    itemGroupKey: "group",
    itemTimeStartKey: "start_time",
    itemTimeEndKey: "end_time",
    groupLabelKey: "title"
};

class TimelineJourney extends Component {

    state = {
        solution: this.props.solution,
        solutions: this.props.solutions,
        isShowConfirm: false,
        showSaveSolutionForm: false,
        isWaiting: false,
        groups: this.props.solution.journeys,
    };

    itemRenderer = ({ item, timelineContext, itemContext, getItemProps, getResizeProps }) => {
        const backgroundColor = itemContext.selected ? (itemContext.dragging ? "red" : item.selectedBgColor) : item.bgColor;
        const borderColor = itemContext.resizing ? "red" : item.color;
        return (
            <div
                {...getItemProps({
                    style: {
                        backgroundColor,
                        color: item.color,
                        borderColor,
                        borderStyle: "solid",
                        borderWidth: 1,
                        borderRadius: 4,
                        borderLeftWidth: itemContext.selected ? 3 : 1,
                        borderRightWidth: itemContext.selected ? 3 : 1
                    },
                    onMouseDown: () => {
                        console.log("on item click", item);
                    },
                    onDoubleClick: () => { this.props.showDepotMarkerInfo(item.marker) },
                })}
            >

                <div
                    style={{
                        height: itemContext.dimensions.height,
                        overflow: "hidden",
                        paddingLeft: 3,
                        textOverflow: "ellipsis",
                        whiteSpace: "nowrap"
                    }}
                >
                    {itemContext.title}
                </div>

            </div>
        );
    };

    setShowConfirm = (isShowConfirm) => {
        this.setState({
            isShowConfirm: isShowConfirm
        })
    }

    handleChangeSubmit = () => {

        this.setState({
            isWaiting: true,
        });

        const updateSolution = {
            solution: this.state.solution,
            changedOrder: this.state.changedOrder,
            toJourney: this.state.toJourney,
            fromJourney: this.state.fromJourney,
            problemAssumption: this.props.problemAssumption,
        }
        console.log("this.props.problemAssumption", this.props.problemAssumption);
        routeService.changeOrderToJourney(updateSolution).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                let solution = data.data;
                solution.id = this.state.solution.id;
                solution.focusJourneyId = 0;
                this.setState({
                    solution: solution,
                    showSuccessMsg: true,
                    successMsg: "Successfully",
                    isShowConfirm: false,
                    isWaiting: false,
                })
                this.updateSolution(solution);
                this.setNodesTimeline(this.state.solution);
                console.log("solution:", solution);
            }

        }).catch(e => {
            console.log(e);
            this.setState({
                showSuccessMsg: true,
                successMsg: "Fail",
                isShowConfirm: false,
                isWaiting: false,
            })
        })

    }

    handleSelectSubmit = () => {

        this.setState({
            isWaiting: true,
        });

        routeService.selectSolution(this.state.solution).then(response => {
            const data = response.data;
            if (data.code === 'SUCCESS') {
                this.setState({
                    showSuccessMsg: true,
                    successMsg: "Chốt lộ trình thành công",
                    isShowSelectSolutionForm: false,
                    isWaiting: false,
                })
                let solution = this.state.solution;
                solution.isSelected = true;
                this.setState({
                    solution: solution,
                });
            }

        })

    }

    updateSolution = (solution) => {
        this.props.setDirections(solution);
        this.props.setSolution(solution);
    }

    setShowSuccessToast = (show) => {
        this.setState({
            showSuccessMsg: show === true ? true : false,
        })
    }

    isItemChangeGroup = (itemId, newGroupOrderIndex) => {
        let isChange = false;
        this.state.nodesTimeline.map((node) => {
            if (node.id === itemId && node.group !== this.state.solution.journeys[newGroupOrderIndex].id)
                isChange = true;
        })
        return isChange;
    }

    handleItemMove = (itemId, dragTime, newGroupOrder) => {
        if (this.isItemChangeGroup(itemId, newGroupOrder)) {
            const changedNodeTimeline = this.state.nodesTimeline.find(nodeTimeline => nodeTimeline.id === itemId);
            const toJourney = this.state.solution.journeys[newGroupOrder];
            const fromJourney = this.state.solution.journeys.find(journey => journey.id === changedNodeTimeline.group);
            this.setState({
                isShowConfirm: true,
                changedOrder: changedNodeTimeline.order,
                toJourney: toJourney,
                fromJourney: fromJourney,
            });
        }
    };

    processJourneys = (solution) => {
        const journeys = solution.journeys.map((journey, index) => {
            journey.id = solution.id * 100 + journey.vehicle.id;
            journey.title = journey.vehicle.name;
            journey.nodesTimeline = this.createNodesTimeline(journey);
            journey.rightTitle = (Math.round((journey.totalTravelTime / 60) * 10) / 10) + " (phút)/" + (Math.round((journey.totalDistance / 1000) * 10) / 10) + " (km)"
            return journey;
        });
        return journeys;
    }

    createNodesTimeline = (journey) => {
        let nodesTimeline = [];
        let startOfDay = moment().startOf('day');
        journey.routes.map((route, index) => {
            let nodesInRoute = [];
            let startDepot = {
                id: 0,
                group: journey.id,
                title: route.startDepot.code,
                code: route.startDepot.code,
                canMove: false,
                canResize: false,
                canChangeGroup: false,
                start_time: startOfDay + route.timeline[0].startTime * 1000,
                end_time: startOfDay + route.timeline[0].endTime * 1000,
                marker: {
                    code: route.startDepot.code,
                    infoWindow: {
                        depotCode: route.startDepot.code,
                        startTime: secondsToHHMMSS(route.timeline[(0)].startTime),
                        endTime: secondsToHHMMSS(route.timeline[(0)].endTime),
                        fillRateCapacity: route.fillRateCapacity,
                        fillRateLoadWeight: route.fillRateLoadWeight,
                        ordinalLoadingOrders: route.ordinalLoadingOrders,
                    }
                },
            }
            startDepot.itemProps = {
                onDoubleClick: () => { this.props.showDepotMarkerInfo(startDepot.marker) },
            }
            nodesInRoute.push(startDepot);
            route.bills.map((bill, index) => {
                let customerTimeline = {
                    id: index,
                    group: journey.id,
                    title: bill.order.customer.code,
                    code: bill.order.customer.code,
                    start_time: startOfDay + route.timeline[(index + 1)].startTime * 1000,
                    end_time: startOfDay + route.timeline[(index + 1)].endTime * 1000,
                    canMove: true,
                    canResize: false,
                    canChangeGroup: true,
                    order: bill.order,
                    marker: {
                        code: bill.order.customer.code,
                        infoWindow: {
                            customerCode: bill.order.customer.code,
                            startTime: secondsToHHMMSS(route.timeline[(index + 1)].startTime),
                            endTime: secondsToHHMMSS(route.timeline[(index + 1)].endTime),
                            intendStartTime: secondsToHHMMSS(bill.order.deliveryAfterTime),
                            intendEndTime: secondsToHHMMSS(bill.order.deliveryBeforeTime),
                            bill: bill,
                            fillRateCapacity: bill.fillRateCapacity,
                            fillRateLoadWeight: bill.fillRateLoadWeight,
                            travelTime: route.timeline[(index + 1)].startTime - route.timeline[(index)].endTime,
                        }
                    },
                }
                customerTimeline.itemProps = {
                    onDoubleClick: () => { this.props.showCustomerMarkerInfo(customerTimeline.marker) },
                }
                nodesInRoute.push(customerTimeline);
            });
            let endDepot = {
                id: nodesInRoute.length,
                group: journey.id,
                title: route.endDepot.code,
                code: route.endDepot.code,
                canMove: false,
                canResize: false,
                canChangeGroup: false,
                start_time: startOfDay + route.timeline[route.timeline.length - 1].startTime * 1000,
                end_time: startOfDay + route.timeline[route.timeline.length - 1].startTime * 1000 + 600 * 1000,
                marker: {
                    code: route.endDepot.code,
                    infoWindow: {
                        depotCode: route.endDepot.code,
                        startTime: secondsToHHMMSS(route.timeline[route.timeline.length - 1].startTime),
                        endTime: secondsToHHMMSS(route.timeline[route.timeline.length - 1].startTime),
                        // fillRateCapacity: route.fillRateCapacity,
                        // fillRateLoadWeight: route.fillRateLoadWeight,
                        // ordinalLoadingOrders: route.ordinalLoadingOrders,
                    }
                },
            }
            endDepot.itemProps = {
                onDoubleClick: () => { this.props.showDepotMarkerInfo(endDepot.marker) },
            }
            nodesInRoute.push(endDepot);
            nodesTimeline = nodesTimeline.concat(nodesInRoute);
        });
        return nodesTimeline;
    }

    setNodesTimeline(solution) {
        let nodesTimeline = [];
        solution.journeys = this.processJourneys(solution);
        solution.journeys.map((journey) => {
            nodesTimeline = nodesTimeline.concat(journey.nodesTimeline);
        });
        nodesTimeline.map((node, index) => {
            node.id = 1000 * solution.id + (index + 1);
        })
        this.setState({ nodesTimeline: nodesTimeline });
    }


    componentDidMount() {
        this.setState({
            solution: this.props.solution,
            solutions: this.props.solutions
        });
        this.setNodesTimeline(this.state.solution);
    }

    resetFocus() {
        let solution = this.state.solution;
        solution.focusJourneyId = 0;
        this.setState({
            solution: solution
        });
    }

    handleChangeSolution = (event) => {
        this.setState({ nodesTimeline: undefined });
        let solutionId = parseInt(event.target.value);
        this.resetFocus();
        this.state.solutions.map((solution) => {
            if (solution.id === solutionId) {
                this.setState({
                    solution: solution,
                    groups: solution.journeys,
                });
                this.setNodesTimeline(solution);
                this.props.setSolution(solution);
            }
        });
    }

    handleChangeJourney = (event) => {
        let journeyId = parseInt(event.target.value);
        const focusJourney = this.state.solution.journeys.find(journey => journey.id === journeyId);
        // let solution =  this.state.solution;
        // solution.focusJourneyId = journeyId;
        if (focusJourney !== undefined) {
            console.log("focusJourney", focusJourney);
            this.props.focusJourney(focusJourney);
            console.log("this.state.solution", this.state.solution);
            this.setState({
                groups: [focusJourney],
                focusJourney: focusJourney,
            });
        }
        else {
            this.props.showAllJourney();
            this.setState({
                groups: this.state.solution.journeys
            });
        }
    }

    showSuccessMsg = (msg) => {
        this.setState({
            showSuccessMsg: true,
            successMsg: msg,
        })
    }

    setShowSaveSolutionForm = (showSaveSolutionForm) => {
        this.setState({
            showSaveSolutionForm: showSaveSolutionForm
        })
    }

    setShowSelectSolutionForm = (showSelectSolutionForm) => {
        this.setState({
            isShowSelectSolutionForm: showSelectSolutionForm
        })
    }

    render() {
        const groups = this.state.groups;
        const items = this.state.nodesTimeline;
        console.log("items", items);
        console.log("groups", groups);
        const { t } = this.props;
        return (
            <CContainer sm="12">
                <CCol sm="12" lg="6">
                    <CToaster position='top-right'>
                        <CToast
                            key={'toastSuccess'}
                            show={this.state.showSuccessMsg}
                            autohide={4000}
                            fade={true}
                            onStateChange={(show) => { this.setShowSuccessToast(show) }}
                        >
                            <CToastHeader closeButton>{t("timeline.notification.title")}</CToastHeader>
                            <CToastBody>
                                {this.state.successMsg}
                            </CToastBody>
                        </CToast>
                    </CToaster>
                </CCol>
                <CRow>
                    {this.state.isShowConfirm === true &&
                        <CModal
                            show={true}
                            onClose={() => this.setShowConfirm(false)}
                            color="warning"
                        >
                            <CModalHeader closeButton>
                                <CModalTitle>{t("timeline.changeOrder.title")}</CModalTitle>
                            </CModalHeader>
                            <CModalBody>
                                <h6>{t("timeline.changeOrder.changeOrder")} <b>{this.state.changedOrder.code}</b> {t("timeline.changeOrder.from")} <b>{this.state.fromJourney.vehicle.name}</b> {t("timeline.changeOrder.to")} <b>{this.state.toJourney.vehicle.name}</b> ?</h6>
                            </CModalBody>
                            <CModalFooter>
                                {this.state.isWaiting === true &&
                                    <CSpinner size='sm' color='green'></CSpinner>
                                }
                                <CButton color="success" onClick={() => this.handleChangeSubmit()}>{t("timeline.changeOrder.yes")}</CButton>{' '}
                                <CButton color="secondary" onClick={() => this.setShowConfirm(false)}>{t("timeline.changeOrder.cancel")}</CButton>
                            </CModalFooter>
                        </CModal>
                    }

                    {this.state.showSaveSolutionForm === true &&
                        <SaveSolutionRouting
                            solution={this.state.solution}
                            problemAssumption={this.props.problemAssumption}
                            setShowSaveSolutionForm={this.setShowSaveSolutionForm}
                            showSuccessMsg={this.showSuccessMsg}
                        ></SaveSolutionRouting>
                    }

                    {this.state.isShowSelectSolutionForm === true &&
                        <CModal
                            show={true}
                            onClose={() => this.setShowSelectSolutionForm(false)}
                            color="warning"
                        >
                            <CModalHeader closeButton>
                                <CModalTitle>Confirm Select Route</CModalTitle>
                            </CModalHeader>
                            <CModalBody>
                                <h6>Xác nhận chốt lộ trình <b>{this.state.solution.name}</b>  ?</h6>
                                <i>Các lộ trình sẽ được gán tự động cho các xe</i>
                            </CModalBody>
                            <CModalFooter>
                                {this.state.isWaitingSelecting === true &&
                                    <CSpinner size='sm' color='green'></CSpinner>
                                }
                                <CButton color="success" onClick={() => this.handleSelectSubmit()}>Confirm</CButton>{' '}
                                <CButton color="secondary" onClick={() => this.setShowSelectSolutionForm(false)}>Cancel</CButton>
                            </CModalFooter>
                        </CModal>
                    }

                </CRow>
                <CRow>
                    <CCol >
                        <CCard>
                            <CCardHeader>
                                <CRow>
                                    <CCol sm='4'>
                                        <CFormGroup row>
                                            {this.state.solution &&
                                                <CCol md='5'>
                                                    <CLabel htmlFor="solution">{t("timeline.changeSolution.title") + this.state.solution.id}</CLabel>
                                                </CCol>
                                            }
                                            {this.state.nodesTimeline &&
                                                <CCol sm='5'>
                                                    <CSelect custom size="sm" name="solution" id="solution" value={this.state.solution.id} onChange={this.handleChangeSolution} >
                                                        {this.state.solutions.map((item, index) => <option key={index} value={item.id}>{t("timeline.changeSolution.solution") + (item.id)}</option>)}
                                                    </CSelect>
                                                </CCol>
                                            }
                                        </CFormGroup>
                                    </CCol>

                                    <CCol sm='5'>
                                        <CFormGroup row>
                                            {this.state.solution &&
                                                <CCol md='3'>
                                                    <CLabel htmlFor="journey">{t("timeline.changeJourney.title")+":"}</CLabel>
                                                </CCol>
                                            }
                                            {this.state.solutions &&
                                                <CCol sm='6'>
                                                    <CSelect custom size="sm" name="journey" id="journey" value={this.state.solution.focusJourneyId} onChange={this.handleChangeJourney} >
                                                        <option key={0} value={0}>{t("timeline.changeJourney.allJourney")}</option>
                                                        {this.state.solution.journeys.map((journey, index) => <option key={index + 1} value={journey.id}>{(journey.vehicle.name)}</option>)}
                                                    </CSelect>
                                                </CCol>
                                            }
                                        </CFormGroup>
                                    </CCol>
                                    <CCol sm='3'>
                                        {
                                            // this.state.solution.isSelected === false &&
                                            //     <CButton color="warning" onClick={() => this.setShowSelectSolutionForm(true)}>
                                            //         Select
                                            //     </CButton>
                                        }
                                        {' '}
                                        <CButton color="success" onClick={() => this.setShowSaveSolutionForm(true)}>
                                           {t("timeline.save")}
                                        </CButton>
                                        {' '}
                                        {/* <CButton color="primary" >
                                            Export
                                        </CButton> */}

                                    </CCol>
                                </CRow>
                                {this.state.solution.focusJourneyId === 0 &&
                                    <CRow>
                                        <CCol sm='2'>{t("timeline.totalCost") + Math.round((this.state.solution.totalCost) * 10) / 10 + " (VND)"}</CCol>
                                        <CCol sm='2'>{t("timeline.totalAmount") + this.state.solution.totalAmount + " (VND)"}</CCol>
                                        <CCol sm='2'>{t("timeline.revenue")+ Math.round((this.state.solution.revenue) * 10) / 10 + " (VND)"}</CCol>
                                        <CCol sm='2'>{t("timeline.efficiency") + Math.round((this.state.solution.efficiency) * 100) / 100 + "%"}</CCol>
                                        <CCol sm='2'>{t("timeline.numberVehicle") + this.state.solution.numberVehicle + t("timeline.numberVehicleUnit")}</CCol>
                                    </CRow>
                                }
                                {this.state.solution.focusJourneyId === 0 &&
                                    <CRow>
                                        <CCol sm='3'>{t("timeline.averageNumOrder") + Math.round(((this.state.solution.totalNumberOrders) / this.state.solution.numberVehicle)) + t("timeline.averageNumOrderUnit") }</CCol>
                                        <CCol sm='3'>{t("timeline.averageTravelTime") + (Math.round(((this.state.solution.totalTravelTime / 60) / this.state.solution.numberVehicle) * 10) / 10) + t("timeline.averageTravelTimeUnit") }</CCol>
                                        <CCol sm='3'>{t("timeline.averageDistance") + Math.round(((this.state.solution.totalDistance / 1000) / this.state.solution.numberVehicle) * 10) / 10 + t("timeline.averageDistanceUnit") }</CCol>
                                    </CRow>
                                }
                                {this.state.solution.focusJourneyId !== 0 &&
                                    <CRow>
                                        <CCol sm='2'>{t("timeline.focusJourney.totalCost") + Math.round((this.state.focusJourney.totalCost) * 10) / 10 + " (VND)"}</CCol>
                                        <CCol sm='2'>{t("timeline.focusJourney.penaltyCost") + Math.round((this.state.focusJourney.totalPenaltyCost) * 10) / 10 + " (VND)"}</CCol>
                                        <CCol sm='2'>{t("timeline.focusJourney.totalAmount") + this.state.focusJourney.totalAmount + " (VND)"}</CCol>
                                        <CCol sm='2'>{t("timeline.focusJourney.revenue") + Math.round((this.state.focusJourney.revenue) * 10) / 10 + " (VND)"}</CCol>
                                        <CCol sm='2'>{t("timeline.focusJourney.efficiency") + Math.round((this.state.focusJourney.revenue * 100 / this.state.focusJourney.totalAmount) * 100) / 100 + "%"}</CCol>
                                    </CRow>
                                }
                                {this.state.solution.focusJourneyId !== 0 &&
                                    <CRow>
                                        <CCol sm='2'>{t("timeline.focusJourney.orderNum") + getCustomersOfJourney(this.state.focusJourney).length + t("timeline.focusJourney.orderNumUnit")}</CCol>
                                        <CCol sm='2'>{t("timeline.focusJourney.routeNum") + this.state.focusJourney.routes.length + t("timeline.focusJourney.routeNumUnit")}</CCol>
                                    </CRow>
                                }
                            </CCardHeader>

                            <CCardBody>
                                {this.state.nodesTimeline && groups &&
                                    <Timeline
                                        groups={groups}
                                        items={items}
                                        keys={keys}
                                        rightSidebarWidth={150}
                                        defaultTimeStart={moment().startOf('day').add(8, 'hour')}
                                        defaultTimeEnd={moment().startOf('day').add(12, 'hour')}
                                        minZoom={1 * 60 * 60 * 1000}
                                        maxZoom={4 * 60 * 60 * 1000}
                                        traditionalZoom
                                        onItemMove={this.handleItemMove}
                                    // itemRenderer={this.itemRenderer}

                                    >
                                        <TimelineHeaders>
                                            <SidebarHeader>
                                                {({ getRootProps }) => {
                                                    return <div {...getRootProps(items)} >
                                                        <div style={{ textAlign: "center" }}>
                                                            <h6 style={{ color: "#FFFAF0" }}>
                                                               {t("timeline.vehicleName")}
                                                            </h6>
                                                        </div>
                                                    </div>
                                                }}
                                            </SidebarHeader>
                                            <SidebarHeader variant="right">
                                                {({ getRootProps }) => {
                                                    return <div {...getRootProps()} >
                                                        <div style={{ textAlign: "center" }}>
                                                            <h6 style={{ color: "#FFFAF0" }}>
                                                            {t("timeline.rightTitle")}
                                                            </h6>
                                                        </div>
                                                    </div>
                                                }}
                                            </SidebarHeader>
                                            <DateHeader unit="primaryHeader" labelFormat="YYYY"/>
                                            <DateHeader unit="hour" labelFormat="HH:mm" />

                                        </TimelineHeaders>
                                    </Timeline>
                                }
                            </CCardBody>
                        </CCard>
                    </CCol>
                </CRow>
            </CContainer >
        )

    }
}
export default withNamespaces('solution')(TimelineJourney);
