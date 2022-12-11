import React, { Component } from "react";
import { getIconURL, getOrdinalLoadingOrders } from "../../../utilities/utility";
const { Marker, InfoWindow } = require("react-google-maps");

class VehicleMarker extends Component {

    constructor(props) {
        super(props);
        this.state = {
            id: this.props.marker.id,
            lat: this.props.marker.lat,
            lng: this.props.marker.lng,
            infoWindow: this.props.marker.infoWindow,
            color: this.props.marker.color,
            icon: getIconURL(this.props.marker.type),
            isShowInfo: false,
        }
    }

    setShowMarkerInfo = (isShowInfo) => {
        this.setState({ isShowInfo: isShowInfo });
    }

    render() {
        console.log("infoWindow: " + this.state.infoWindow)
        console.log(this.props.marker)
        return (
            <Marker
                key={this.state.id}
                icon={this.state.icon}
                onClick={() => this.setShowMarkerInfo(true)}
                position={{
                    lat: parseFloat(this.state.lat),
                    lng: parseFloat(this.state.lng),
                }}
            >
                {this.state.isShowInfo === true && this.props.marker.infoWindow &&
                    <InfoWindow onCloseClick={() => this.setShowMarkerInfo(false)}>
                        {this.props.marker.infoWindow &&
                            <div>
                                <div>
                                    <p><b>{"Tên xe: "}</b>{this.props.marker.infoWindow.vehicle.name}</p>
                                </div>
                                <div>
                                    <p><b>{"Tên lái xe: "}</b>{this.props.marker.infoWindow.vehicle.driverName}</p>
                                </div>
                                <div>
                                    <p><b>{"Các đơn hàng cần giao: "}</b>{getOrdinalLoadingOrders(this.props.marker.infoWindow.mustDeliveryOrders)}</p>
                                </div>
                                <div>
                                    <p><b>{"Tọa độ hiện tại: "}</b>{this.props.marker.infoWindow.actualJourney.currentLat + "-" + this.props.marker.infoWindow.actualJourney.currentLng}</p>
                                </div>
                                <div>
                                    <p><b>{"Đơn hàng tiếp theo: "}</b>{this.props.marker.infoWindow.actualJourney.nextOrder.code}</p>
                                </div>
                                <div>
                                    <p><b>{"Khách hàng tiếp theo: "}</b>{this.props.marker.infoWindow.actualJourney.nextOrder.customer.code}</p>
                                </div>
                                {/* <div>
                                    <p><b>{"Tỷ lệ lấp đầy thể tích: "}</b>{this.props.marker.infoWindow.fillRateCapacity + " %"}</p>
                                </div>
                                <div>
                                    <p><b>{"Tỷ lệ lấp đầy khối lượng: "}</b>{this.props.marker.infoWindow.fillRateLoadWeight + " %"}</p>
                                </div>
                                <div>
                                    <p><b>{"Số đơn đã giao: "}</b>{this.props.marker.infoWindow.fillRateLoadWeight + " %"}</p>
                                </div>
                                <div>
                                    <p><b>{"Số đơn chưa giao: "}</b>{this.props.marker.infoWindow.fillRateLoadWeight + " %"}</p>
                                </div>
                                <div>
                                    <p><b>{"Thời gian chạy: "}</b>{this.props.marker.infoWindow.fillRateLoadWeight + " %"}</p>
                                </div>
                                <div>
                                    <p><b>{"Quãng đường chạy: "}</b>{this.props.marker.infoWindow.fillRateLoadWeight + " %"}</p>
                                </div> */}
                            </div>

                        }

                    </InfoWindow>
                }
            </Marker>);
    }
}

export default VehicleMarker

