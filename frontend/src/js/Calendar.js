import React from "react";
import "../css/Calendar.css";
import EventForm from "./FormEvent";
import {Avatar, Button, DatePicker, Layout, List} from "antd";
import moment from "moment";
import * as conf from './config.js'


const WeekPicker = DatePicker.WeekPicker;
const {Content} = Layout;

moment.locale("en-wow-settings", {
  week: {
    dow: 3
  }
});

var displaytWeek = moment();

function nextWeek() {
  console.log("Next Week called");
  weekPickerChange.call(this, displaytWeek.add(7, "days"));
}

function previousWeek() {
  console.log("Previous Week called");
  weekPickerChange.call(this, displaytWeek.subtract(7, "days"));
}

function weekPickerChange(date) {
  displaytWeek = moment().hour(0).minute(0).second(0).day("Wednesday").week(date.week()).utc(true);

  let url = conf.baseURL + "/eventByDate";
  url = url + "/" + moment().hour(0).minute(0).second(0).day("Wednesday").week(date.week()).utc(true).unix() + "/" + moment().hour(0).minute(0).second(0).day("Wednesday").week(date.week()+1).utc(true).unix();

  console.log(url);



  fetch(url, {
    method: "GET",
  })
    .then(results => results.json())
    .then(data => this.setState({meetings: data}))
    .catch(function (error) {
      console.log(
        "There was an error GET eventByDate, weekPicker: /// " + error + " \\\\\\"
      );
    });
}

class Calendar extends React.Component {
  constructor() {
    super();
    this.state = {
      meetings: [],
      formVisible: false
    };
  }

  showModal = () => {
    this.setState({formVisible: true});
  };
  handleCancel = () => {
    this.setState({formVisible: false});
  };
  handleCreate = () => {
    const form = this.formRef.props.form;
    form.validateFields((err, values) => {
      if (err) {
        return;
      }

      console.log("Received values of form: ", values);

      let time_begin = values["date-picker"]
        .utc()
        .set({
          hour: values["time-begin"].get("hour"),
          minute: values["time-begin"].get("minute"),
          second: 0
        })
        .unix();

      let time_end = values["date-picker"]
        .utc()
        .set({
          hour: values["time-end"].get("hour"),
          minute: values["time-end"].get("minute"),
          second: 0
        })
        .unix();


      fetch(conf.baseURL + '/event', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          name: values.title,
          description: values.description,
          meeting: {
            time_begin: time_begin + "",
            time_end: time_end + ""
          },
          raid: {
            name: values.raid,
            nb_boss: 0,
            difficulty: values.difficulty
          },
          roster: {
            name: "Derp Roster"
          }
        })
      })
        .then(results => results.json())
        .catch(function (error) {
          console.log(
            "There was an error POST meeting: /// " + error + " \\\\\\"
          );
        });

      form.resetFields();
      this.setState({formVisible: false});
      weekPickerChange.call(this, displaytWeek);

    });
  };
  saveFormRef = formRef => {
    this.formRef = formRef;
  };


  componentDidMount() {
    let url = conf.baseURL + "/eventByDate";
    url = url + "/" + moment().hour(0).minute(0).second(0).day("Wednesday").week(moment().week()).utc(true).unix() + "/" + moment().hour(0).minute(0).second(0).day("Wednesday").week(moment().week()+1).utc(true).unix();

    console.log(url);

    fetch(url, {
      method: "GET",
    })
      .then(results => results.json())
      .then(data => this.setState({meetings: data}))
      .catch(function (error) {
        console.log(
          "There was an error GET eventByDate, componentDidMount: /// " + error + " \\\\\\"
        );
      });
  }

  render() {
    return (
      <Content style={{margin: "16px 16px"}}>
        <div style={{padding: 24, background: "#fff", minHeight: 360}}>

          <div className="footer-container">
            <div className="div-left">
              <ul className="footer">
                <Button.Group>
                  <Button onClick={previousWeek.bind(this)} icon="left"/>
                  <WeekPicker id="weekpicker" onChange={weekPickerChange.bind(this)} format={"wo-YYYY"} value={displaytWeek}/>
                  <Button onClick={nextWeek.bind(this)} icon="right"/>
                </Button.Group>
              </ul>
            </div>
            <div className="div-right">
              <ul className="footer">
                <Button type="primary" onClick={this.showModal}>
                  New Event
                </Button>
                <EventForm
                  wrappedComponentRef={this.saveFormRef}
                  visible={this.state.formVisible}
                  onCancel={this.handleCancel}
                  onCreate={this.handleCreate.bind(this)}
                />
              </ul>
            </div>
          </div>

          <List
            itemLayout="vertical"
            size="large"
            pagination={{
              onChange: page => {
                console.log(page);
              },
              pageSize: 5
            }}
            dataSource={this.state.meetings}
            renderItem={item => (
              <List.Item
                key={item.id}
                /*actions={[
                  <IconText type="star-o" text="156"/>,
                  <IconText type="like-o" text="156"/>,
                  <IconText type="message" text="2"/>
                ]}*/
                extra={
                  <img
                    width={272}
                    alt="logo"
                    src="https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png"
                  />
                }
              >
                <List.Item.Meta
                  avatar={<Avatar src={item.avatar}/>}
                  title={<a href={item.href}>{item.name} - {moment(item.meeting.time_begin, "X").format("dddd Do MMMM YYYY hh:mm")}</a>}
                  description={<div>{item.raid.name} - {item.raid.difficulty}</div>}
                />

                <div>
                  <p>{item.description}</p>

                  <p><b>Roster :</b> {item.roster.name}</p>

                  {/*<List
                    size="small"
                    header={<div>Rosters</div>}
                    dataSource={item.roster}
                    renderItem={rosterList => (<List.Item>{rosterList.name}</List.Item>)}
                  />*/}

                </div>
              </List.Item>
            )}
          />
        </div>
      </Content>
    );
  }
}

export default Calendar;
